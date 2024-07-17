import './style.css'
import defaultProfileImage from "assets/image/user-default-image.png"
import {BoardListItem, User} from "../../types/interface";

interface Props {
  user: User;
  boardListItem: BoardListItem;
  onClick: () => void;
}

export default function BoardItem({boardListItem, user,onClick}: Props) {

  const {title, content, boardTitleImage} = boardListItem
  const {viewCount} = boardListItem;
  const {createdAt} = boardListItem
  const {nickname, profileImage} = user;


  const formattedDateTime = new Date(createdAt).toLocaleDateString('en-CA', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false
  });

  //     render : Board List Item 컴포턴트 렌더링    //
  return (
      <div className='board-list-item' onClick={onClick}>
        <div className='board-list-item-main-box'>
          <div className='board-list-item-top'>
            <div className='board-list-item-profile-box'>
              <div className='board-list-item-profile-image'
                   style={{backgroundImage: `url(${profileImage ? profileImage : defaultProfileImage})`}}></div>
            </div>
            <div className='board-list-item-write-box'>
              <div className='board-list-item-nickname'>{nickname}</div>
              <div className='board-list-item-write-date'>{formattedDateTime}</div>
            </div>
          </div>
          <div className='board-list-item-middle'>
            <div className='board-list-item-title'>{title}</div>
            <div className='board-list-item-content'>{content}</div>
          </div>
          <div className='board-list-item-bottom'>
            <div
                className='board-list-item-counts'>{`조회수 ${viewCount} `}</div>
          </div>
        </div>
        {boardTitleImage !== null && (
            <div className='board-list-item-image-box'>
              <div className='board-list-item-image'
                   style={{backgroundImage: `url(${boardTitleImage}`}}></div>
            </div>
        )}
      </div>
  );
}