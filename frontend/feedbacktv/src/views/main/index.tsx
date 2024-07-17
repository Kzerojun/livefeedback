import React, {useEffect, useState} from 'react';
import {Avatar, Card, Layout, Menu, MenuProps} from 'antd';
import './style.css';
import {getBroadcastsByCategory} from "../../apis/broadcast";
import BroadcastsInfoResponseDto from "../../types/response/broadcast/broadcasts-info.response.dto";
import ResponseDto from "../../types/response/response.dto";
import {HomeOutlined,StarOutlined} from '@ant-design/icons';
import Meta from "antd/es/card/Meta";
import { STREAM_PATH} from "../../constants/paths";
import defaultProfileImage from "../../assets/image/user-default-image.png";
import {useNavigate} from "react-router-dom";
import Broadcast from "../../types/interface/broadcast/broadcast.interface";
import {MAIN_CATEGORIES} from "../../constants/main/mainConstants";

const {Header, Sider, Content} = Layout;


export default function Main() {
  const [broadcasts, setBroadcasts] = useState<Broadcast[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<string>('ALL');
  const [noBroadcastsMessage, setNoBroadcastsMessage] = useState<string>('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchBroadcasts(selectedCategory);
  }, [selectedCategory]);

  const fetchBroadcasts = async (category: string) => {
    const response = await getBroadcastsByCategory(category);
    getBroadcastsInfoResponse(response);
  };

  const getBroadcastsInfoResponse = (response: BroadcastsInfoResponseDto | ResponseDto | null) => {
    if (!response) {
      return;
    }

    const {code} = response;

    if (code !== 'SU') {
      alert("에러발생");
      return;
    }

    const {broadcastInfos} = response as BroadcastsInfoResponseDto;

    if (!broadcastInfos || broadcastInfos.length === 0) {
      setBroadcasts([]);
      setNoBroadcastsMessage("방송이 없습니다");
      return;
    }

    setBroadcasts(broadcastInfos);
    setNoBroadcastsMessage('');
  };

  const onClickMenuHandler: MenuProps['onClick'] = (e) => {
    console.log('click ', e);
    setSelectedCategory(e.key);
  };

  const items = [
    {
      label: '홈',
      key: '홈',
      icon : <HomeOutlined/>,
      children: MAIN_CATEGORIES.map(category => ({
        key: category.value,
        label: category.label
      }))
    },
    {label: '즐겨찾기',
      key: 'Favorite',
    icon : <StarOutlined />}
  ];

  const onCardClickHandler = (streamerId : string, broadcastId : number) =>{
    navigate(STREAM_PATH(streamerId, broadcastId));
  }

  return (
      <Layout className="main-layout">
        <Sider style={{background: "white"}} width={300}>
          <Menu
              theme="light"
              defaultSelectedKeys={['ALL']}
              style={{marginTop: 30}}
              mode="inline"
              items={items}
              onClick={onClickMenuHandler}
              defaultOpenKeys={['홈']}
          />
        </Sider>
        <Layout>
          <Header style={{background: "white"}}>
          </Header>
          <Content style={{background: '#fff'}} className="main-content">
            <div className='category-title'>
              {selectedCategory}
            </div>
            <div className='broadcast-list'>
              {broadcasts.length > 0 ? (
                  broadcasts.map(broadcast => (
                      <Card
                          key={broadcast.broadcastId}
                          style={{width: 400, height: 200, marginBottom: 20}}
                          cover={
                            <img
                                alt={broadcast.title}
                                src={`${broadcast.thumbnail}`}
                            />
                          }
                          onClick={() => onCardClickHandler(broadcast.streamerId, broadcast.broadcastId)}
                      >
                        <Meta
                            avatar={<Avatar src={broadcast.profileImage || defaultProfileImage}/>}
                            title={broadcast.title}
                            description={broadcast.streamerNickname}
                        />
                      </Card>
                  ))
              ) : (
                  <div>{noBroadcastsMessage}</div>
              )}
            </div>
          </Content>
        </Layout>
      </Layout>
  );
}
