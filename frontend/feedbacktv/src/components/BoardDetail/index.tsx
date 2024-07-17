import React, {useEffect, useState} from "react";
import {getBoardDetail} from "../../apis/board";
import GetBoardDetailResponseDto from "../../types/response/board/get-board-detail.response.dto";
import ResponseDto from "../../types/response/response.dto";
import './style.css';
import videojs from "video.js";
import on = videojs.on;

interface Props {
  boardId : number;
  onClose : () => void;
}
export default function BoardDetail(props : Props) {

  const {boardId} = props;
  const{onClose} = props

  const [title, setTitle] = useState<string>('');
  const [content, setContent] = useState<string>('');
  const [images, setImages] = useState<string[]>([]); // 이미지 상태 추가

  const getBoardDetailResponse = (responseBody : GetBoardDetailResponseDto | ResponseDto | null) =>{
    if (!responseBody) {
      return;
    }

    const {code} = responseBody;

    if(code === 'NB') {
      alert('게시물이 존재하질 않습니다.');
      return;
    }

    if (code !== 'SU') {
      alert('에러가 발생했습니다. 여기');
      return;
    }

    const result = responseBody as GetBoardDetailResponseDto;

    setTitle(result.title);
    setContent(result.content);
    setImages(result.images || []); // 이미지를 상태에 설정 (null 검사 포함)
  }

  useEffect(() => {
    getBoardDetail(boardId).then(getBoardDetailResponse)
  }, [boardId]);

  return (
      <div id="board-detail-wrapper">
        <div className="board-detail-container">
          <div className="board-detail-box">
            <div className='board-detail-option'>
              <div className='board-detail-close-box' onClick={onClose}>
                <div className='icon close-icon'/>
              </div>
            </div>
            <div className="board-detail-title-box">
              <div className='board-detail-title-textarea'>
                {title}
              </div>

            </div>
            <div className="divider"></div>
            <div className="board-detail-content-box">
              {content}
            </div>
            <div className="board-detail-images-box">
              {images.map((image, index) => (
                  <div className='board-detail-image-box'>
                    <div className='board-detail-image'>
                      <img
                          src={`${image}`}/>
                    </div>
                  </div>
              ))}
            </div>
          </div>
        </div>
      </div>
  );

}