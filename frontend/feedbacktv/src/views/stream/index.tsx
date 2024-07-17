import React, { useState, useRef, useEffect } from 'react';
import Hls from 'hls.js';
import './style.css';
import {useLocation, useNavigate, useParams} from 'react-router-dom';
import webSocketService from "../../apis/websocket";
import { IMessage } from "@stomp/stompjs";
import { getBroadcastInfo } from "../../apis/broadcast";
import { StreamingBroadcastInfoResponseDto } from "../../types/response/broadcast";
import ResponseDto from "../../types/response/response.dto";
import { message } from 'antd';
import { CaretRightOutlined, PauseOutlined, SoundOutlined } from '@ant-design/icons';
import useLoginUserStore from "../../stores/login-user.store";
import {useCookies} from "react-cookie";

interface UserInfo {
  username: string;
  profileImage: string;
  description: string;
}

interface ChatMessage {
  nickname: string;
  content: string;
}

export default function Stream() {
  const { streamerId, broadcastId } = useParams<string>();
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [newMessage, setNewMessage] = useState<string>('');
  const [isLive, setIsLive] = useState<boolean>(true);
  const [isPlaying, setIsPlaying] = useState<boolean>(false);
  const [streamKey, setStreamKey] = useState<string>('');
  const [streamerNickname, setStreamerNickname] = useState<string>('');
  const [broadcastTitle, setBroadcastTitle] = useState<string>('');
  const [volume, setVolume] = useState<number>(0); // 초기 볼륨을 0으로 설정
  const videoRef = useRef<HTMLVideoElement>(null);

  const location = useLocation();
  const {loginUser} = useLoginUserStore();
  const [cookies, setCookie] = useCookies();

  const userInfo: UserInfo = {
    username: '방송국 이름',
    profileImage: 'https://via.placeholder.com/150',
    description: '방송 설명을 여기에 입력하세요.'
  };


  const getStreamingBroadcastResponse = (responseBody: StreamingBroadcastInfoResponseDto | ResponseDto | null) => {
    if (!responseBody) {
      setIsLive(false);
      return;
    }

    const { code } = responseBody;

    if (code !== 'SU') {
      message.error("에러 발생했습니다.");
      setIsLive(false);
      return;
    }

    const response = responseBody as StreamingBroadcastInfoResponseDto;
    setIsLive(true);
    setStreamKey(response.streamKey);
    setBroadcastTitle(response.title);
    setStreamerNickname(response.streamerNickname);
  };

  useEffect(() => {
    const topic = `/topic/${streamerId}/${broadcastId}`;

    webSocketService.activate();
    webSocketService.subscribe(topic, (message: IMessage) => {
      if (message.body) {
        const chatMessage: ChatMessage = JSON.parse(message.body);
        setMessages(prevMessages => [...prevMessages, chatMessage]);
      }
    });

    return () => {
      if (webSocketService.isConnected()) {
        webSocketService.unsubscribe(topic);
        webSocketService.deactivate(); // 모든 구독 해제 및 연결 종료
      }
    };
  }, [streamerId, broadcastId]);

  useEffect(() => {
    const fetchBroadcastInfo = async () => {
      if (!broadcastId) return;

      const numericBroadcastId = parseInt(broadcastId, 10);

      try {
        const getBroadcastInfoResponse = await getBroadcastInfo(numericBroadcastId);
        getStreamingBroadcastResponse(getBroadcastInfoResponse);
      } catch (error) {
        alert("스트림 상태를 확인하는 중 오류가 발생했습니다.");
      }
    };
    fetchBroadcastInfo();
  }, [broadcastId]);

  useEffect(() => {
    const hls = new Hls({ liveSyncDurationCount: 3 });

    if (videoRef.current) {
      videoRef.current.muted = true; // 비디오를 음소거로 시작
      if (Hls.isSupported()) {
        hls.loadSource(`http://localhost:8088/hls/${streamKey}.m3u8`);
        hls.attachMedia(videoRef.current);
        hls.on(Hls.Events.MANIFEST_PARSED, () => {
          videoRef.current?.play();
          setIsPlaying(true);
        });
      } else if (videoRef.current.canPlayType('application/vnd.apple.mpegurl')) {
        videoRef.current.src = `http://localhost:8088/hls/${streamKey}.m3u8`;
        videoRef.current.addEventListener('loadedmetadata', () => {
          videoRef.current?.play();
          setIsPlaying(true);
        });
      }
    }

    return () => {
      if (hls) {
        hls.destroy();
      }
    };
  }, [streamKey]);

  const handleSendMessage = () => {
    if(!loginUser) {
      message.error("로그인이 필요합니다.");
    }

    if (newMessage.trim()) {
      const sendPath = `/app/chat.sendMessage/${streamerId}/${broadcastId}`;
      webSocketService.sendMessage(sendPath, {
        loginId: loginUser?.userId,
        content: newMessage,
      });
      setNewMessage('');
    }
  };

  useEffect(() => {
    const currentPath = location.pathname;

    return () => {
      if (location.pathname !== currentPath && webSocketService.isConnected()) {
        webSocketService.deactivate();
      }
    };
  }, [location]);



  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewMessage(e.target.value);
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleSendMessage();
    }
  };

  const togglePlayPause = () => {
    if (videoRef.current) {
      if (videoRef.current.paused) {
        videoRef.current.play();
        setIsPlaying(true);
      } else {
        videoRef.current.pause();
        setIsPlaying(false);
      }
    }
  };

  const handleVolumeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newVolume = Number(e.target.value);
    setVolume(newVolume);
    if (videoRef.current) {
      videoRef.current.volume = newVolume;
      videoRef.current.muted = newVolume === 0; // 볼륨이 0이면 음소거 유지
    }
  };

  return (
      <div className="App">
        <div className="video-section">
          <div className="video-container">
            {isLive ? (
                <div className="video-wrapper">
                  <video ref={videoRef} className="video-player" />
                  <div className="video-controls">
                    <button onClick={togglePlayPause} className="control-button">
                      {isPlaying ? <PauseOutlined /> : <CaretRightOutlined />}
                    </button>
                    <SoundOutlined style={{ fontSize: '24px', color: '#fff', margin: '0 10px' }} />
                    <input type="range" min="0" max="1" step="0.05" value={volume} onChange={handleVolumeChange} className="control-slider" />
                  </div>
                </div>
            ) : (
                <div className="offline-message">
                  <div className="offline-overlay">
                    <p>라이브 중이지 않습니다</p>
                  </div>
                </div>
            )}
          </div>
          <div className="streamer-info">
            <img src={userInfo.profileImage} alt="Profile" className="profile-image" />
            <div className="streamer-details">
              <h2>{streamerNickname}</h2>
              <p>{broadcastTitle}</p>
            </div>
          </div>
        </div>
        <div className="chat-section">
          <div className="chat-container">
            <div className="chat-box">
              {messages.map((msg, index) => (
                  <div key={index} className="chat-message">
                    <span className="chat-loginId">{msg.nickname}</span>
                    <span className="chat-content">{': ' + ' '.repeat(2) + msg.content}</span>
                  </div>
              ))}
            </div>
            <div className="chat-input-container">
              <input
                  type="text"
                  value={newMessage}
                  onChange={handleInputChange}
                  onKeyDown={handleKeyDown}
                  placeholder={loginUser ? "Type your message here..." : "로그인이 필요합니다"}
              />
              <button onClick={handleSendMessage}>Send</button>
            </div>
          </div>
        </div>
      </div>
  );
}
