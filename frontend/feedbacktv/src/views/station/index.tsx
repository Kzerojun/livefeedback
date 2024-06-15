import {useNavigate, useParams} from "react-router-dom";
import useLoginUserStore from "../../stores/login-user.store";
import {useCookies} from "react-cookie";
import React, {ChangeEvent, useEffect, useRef, useState} from "react";
import {BoardListItem, Station, User} from "../../types/interface";
import UserBox from "../../components/UserBox";
import defaultStationImage from "assets/image/station-default-image.png"
import './style.css';
import BoardItem from "../../components/BoardItem";
import userBoardStore from "../../stores/board.store";
import BoardCategory from "../../types/interface/board/board-category.interface";
import {GetBoardListResponseDto, PostBoardResponseDto} from "../../types/response/board";
import ResponseDto from "../../types/response/response.dto";
import {GetMemberResponseDto} from "../../types/response/member";
import {GetStationResponseDto} from "../../types/response/station";
import {GetFanListResponseDto} from "../../types/response/donation";
import GetBoardCategoryListResponseDto
  from "../../types/response/boardcategory/get-board-list.response.dto";
import {PostBoardRequestDto} from "../../types/request/board";
import {MAIN_PATH} from "../../constants/paths";
import {fileUploadRequest} from "../../apis/file";
import {getBoardCategoryListRequest, getBoardList, postBoardRequest} from "../../apis/board";
import {getUserRequest} from "../../apis/member";
import {getStationRequest} from "../../apis/station";
import {getTop3FanListRequest} from "../../apis/donation";
import BoardDetail from "../../components/BoardDetail";
import { Button, Checkbox, Divider, Tabs } from 'antd';

const CheckboxGroup = Checkbox.Group;

const operations = <Button>Extra Action</Button>;

const OperationsSlot: Record<PositionType, React.ReactNode> = {
  left: <Button className="tabs-extra-demo-button">Left Extra Action</Button>,
  right: <Button>Right Extra Action</Button>,
};

const options = ['left', 'right'];

type PositionType = 'left' | 'right';

const items = new Array(3).fill(null).map((_, i) => {
  const id = String(i + 1);
  return {
    label: `Tab ${id}`,
    key: id,
    children: `Content of tab ${id}`,
  };
});

export default function Channel() {

  // state : userId path variable 상태  //
  const {streamerId} = useParams();

  // state : 로그인 유저 상태
  const {loginUser} = useLoginUserStore();

  // state : cookie 상태 //
  const [cookies, setCookies] = useCookies();

  //function : 네비게이트 함수  //
  const navigate = useNavigate();

  const [myPage, setMyPage] = useState<boolean>(false);
  const [description, setDescription] = useState<string | null>('');
  const [user, setUser] = useState<User>({
    userId: '',
    profileImage: null,
    nickname: '',
    streamKey: ''
  });
  const [station, setStation] = useState<Station>({description: null, image: null});
  const [fans, setFans] = useState<User[]>([]);
  const [postView, setPostView] = useState<boolean>(false);
  const {title, content, resetBoard} = userBoardStore();
  const [boardCategories, setBoardCategories] = useState<BoardCategory[]>([]);
  const [currentCategory, setCurrentCategory] = useState<string>('자유게시판');
  const [boardList, setBoardList] = useState<BoardListItem[]>([]);
  const [detailView, setDetailView] = useState<boolean>(false);
  const [selectedBoardId, setSelectedBoardId] = useState<number>(0);

  const onPostButtonClickHandler = () => {
    setPostView(true);
  }

  const closeBoardDetailHandler = () => {
    setDetailView(false);
  }

  const onCloseButtonClickHandler = () => {
    setPostView(false);
    resetBoard();
  }

  const onCategoryClickHandler = (category: string) => {
    setCurrentCategory(category);
  }

  const onBoardDetailClickHandler = (boardId : number) => {
    setSelectedBoardId(boardId)
    setDetailView(true);
  }

  // function : post board response 처리 함수 //
  const postBoardResponse = (responseBody: PostBoardResponseDto | ResponseDto | null) => {
    if (!responseBody) return;

    const {code} = responseBody;

    if (code === 'DBE') {
      alert('데이터베이스 오류입니다.');
    }

    if (code === 'AF' || code === 'NEM') {
      navigate(MAIN_PATH());
    }

    if (code === 'VF') {
      alert('제목과 내용과 카테고리는 필수입니다.');
    }

    if (code !== 'SU') return;


    resetBoard();
    if (!streamerId) return;

    setPostView(false);
    window.location.reload();
  }


  // function : get user response 처리 함수  //
  const getUserResponse = (responseBody: GetMemberResponseDto | ResponseDto | null) => {
    if (!responseBody) return;
    const {code} = responseBody;

    if (code === 'NEM') alert('존재하지 않는 유저입니다.');
    if (code === 'DBE') alert('데이터베이스 오류입니다.');

    if (code !== 'SU') {
      navigate(MAIN_PATH());
      return;
    }

    const responseUser = responseBody as GetMemberResponseDto;
    setUser(responseUser);

    const isMyPage = streamerId === loginUser?.userId;
    setMyPage(isMyPage);
  }

  // function : get station response 처리 함수  //
  const getStationResponse = (responseBody: GetStationResponseDto | ResponseDto | null) => {

    if (!responseBody) return;
    const {code} = responseBody;

    if (code === 'NEM') alert('존재하지 않는 유저입니다.');
    if (code === 'NES') alert('존재하지 않는 방송국입니다.');
    if (code === 'DBE') alert('데이터베이스 오류입니다.');

    if (code !== 'SU') {
      navigate(MAIN_PATH());
      return;
    }

    const responseStation = responseBody as GetStationResponseDto;
    setStation(responseStation);
  }


  // function : get fan list response 처리 함수  //
  const getFanListResponse = (responseBody: GetFanListResponseDto | ResponseDto | null) => {

    if (!responseBody) return;
    const {code} = responseBody;

    if (code === 'NEM') alert('존재하지 않는 유저입니다.');
    if (code === 'DBE') alert('데이터베이스 오류입니다.');

    if (code !== 'SU') {
      navigate(MAIN_PATH());
      return;
    }

    const responseFanList = responseBody as GetFanListResponseDto;
    setFans(responseFanList.fans);
  }

  const getBoardCategoryResponse = (responseBody: GetBoardCategoryListResponseDto | ResponseDto | null) => {
    if (!responseBody) return;
    const {code} = responseBody;

    if (code === 'NEM') alert('존재하지 않는 유저입니다.');
    if (code === 'DBE') alert('데이터베이스 오류입니다.');

    if (code !== 'SU') {
      navigate(MAIN_PATH());
      return;
    }
    const {boardCategoryItemList} = responseBody as GetBoardCategoryListResponseDto;
    setBoardCategories(boardCategoryItemList);
    const defaultCategory = boardCategoryItemList.find(category => category.category === '자유게시판');
    if (defaultCategory) {
      setCurrentCategory(defaultCategory.category);
    }
  }

  const getBoardCategoryListResponse = (responseBody: GetBoardListResponseDto | ResponseDto | null) => {
    if (!responseBody) return;
    const {code} = responseBody;

    if (code === 'NEM') alert('존재하지 않는 유저입니다.');
    if (code === 'DBE') alert('데이터베이스 오류입니다.');

    if (code !== 'SU') {
      navigate(MAIN_PATH());
      return;
    }

    const {boardList} = responseBody as GetBoardListResponseDto;
    setBoardList(boardList);
  }

  //component : postBoard //
  const PostBoard = () => {
    const titleRef = useRef<HTMLTextAreaElement | null>(null);
    const contentRef = useRef<HTMLTextAreaElement | null>(null);
    const imageInputRef = useRef<HTMLInputElement | null>(null);

    const [category, setCategory] = useState<string>('');
    const [title, setTitle] = useState<string>('');
    const [content, setContent] = useState<string>('');
    const [imageUrls, setImageUrls] = useState<string[]>([]);
    const [boardImageFileList, setBoardImageFileList] = useState<File[]>([]);

    const onTitleChangeHandler = (event: ChangeEvent<HTMLTextAreaElement>) => {
      const {value} = event.target;
      setTitle(value);
      if (titleRef.current) {
        titleRef.current.style.height = 'auto';
        titleRef.current.style.height = `${titleRef.current.scrollHeight}px`;
      }
    };

    const onContentChangeHandler = (event: ChangeEvent<HTMLTextAreaElement>) => {
      const {value} = event.target;
      setContent(value);
      if (contentRef.current) {
        contentRef.current.style.height = 'auto';
        contentRef.current.style.height = `${contentRef.current.scrollHeight}px`;
      }
    };

    const onClickUploadButtonClickHandler = async () => {
      const accessToken = cookies.accessToken;
      if (!accessToken) {
        return;
      }

      const boardImageList: string[] = [];

      for (const file of boardImageFileList) {
        const data = new FormData();
        data.append('file', file);

        const url = await fileUploadRequest(data);
        if (url) boardImageList.push(url);
      }

      const requestBody: PostBoardRequestDto = {
        title, content, boardImageList, category
      };

      postBoardRequest(requestBody, accessToken).then(postBoardResponse);
    }

    const onImageChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
      if (event.target.files && event.target.files.length) {
        const file = event.target.files[0];
        const imageUrl = URL.createObjectURL(file);
        setImageUrls([...imageUrls, imageUrl]);
        setBoardImageFileList([...boardImageFileList, file]);
      }
      if (imageInputRef.current) {
        imageInputRef.current.value = '';
      }
    };

    const onImageUploadButtonClickHandler = () => {
      imageInputRef.current?.click();
    };

    const onImageCloseButtonClickHandler = (deleteIndex: number) => {
      const newImageUrls = imageUrls.filter((_, index) => index !== deleteIndex);
      const newBoardImageFileList = boardImageFileList.filter((_, index) => index !== deleteIndex);
      setImageUrls(newImageUrls);
      setBoardImageFileList(newBoardImageFileList);
    };
    // 카테고리 선택 핸들러
    const onCategoryChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
      setCategory(event.target.value);
    };

    return (
        <div id="board-write-wrapper">
          <div className="board-write-container">
            <div className="board-write-box">
              <div className='board-write-option'>
                <div className='board-write-close-box'>
                  <div className='icon close-icon' onClick={onCloseButtonClickHandler}/>
                </div>
                <div className='board-write-upload-button'
                     onClick={onClickUploadButtonClickHandler}>
                  {'업로드'}
                </div>
              </div>
              <div className='board-write-category-box'>
                <select onChange={onCategoryChange} value={category}
                        className='board-write-category-option'>
                  <option value="">카테고리를 선택해주세요.</option>
                  {boardCategories.map((item) => (
                      <option key={item.category} value={item.category}>
                        {item.category}
                      </option>
                  ))}
                </select>
              </div>
              <div className="board-write-title-box">
              <textarea
                  className="board-write-title-textarea"
                  ref={titleRef}
                  rows={1}
                  placeholder="제목을 작성해주세요."
                  value={title}
                  onChange={onTitleChangeHandler}
              />
              </div>
              <div className="divider"></div>
              <div className="board-write-content-box">
              <textarea
                  ref={contentRef}
                  className="board-write-content-textarea"
                  placeholder="본문을 작성해주세요."
                  value={content}
                  onChange={onContentChangeHandler}
              />
                <div className="icon-button" onClick={onImageUploadButtonClickHandler}>
                  <div className="icon image-box-light-icon"></div>
                </div>
                <input
                    ref={imageInputRef}
                    type="file"
                    accept="image/*"
                    style={{display: 'none'}}
                    onChange={onImageChangeHandler}
                />
              </div>
              <div className="board-write-images-box">
                {imageUrls.map((imageUrl, index) => (
                    <div className="board-write-image-box" key={index}>
                      <img className="board-write-image" src={imageUrl} alt="Uploaded content"/>
                      <div className="icon-button image-close"
                           onClick={() => onImageCloseButtonClickHandler(index)}>
                        <div className="icon close-icon"></div>
                      </div>
                    </div>
                ))}
              </div>
            </div>
          </div>
        </div>
    );
  };


  useEffect(() => {
    if (!streamerId) return;
    getUserRequest(streamerId).then(getUserResponse);
    getStationRequest(streamerId).then(getStationResponse);
    getTop3FanListRequest(streamerId).then(getFanListResponse);
    getBoardCategoryListRequest(streamerId).then(getBoardCategoryResponse);
  }, [streamerId]);

  useEffect(() => {
    if (!streamerId) return;
    getBoardList(streamerId, currentCategory).then(getBoardCategoryListResponse);
  }, [currentCategory])

  return (
      <div id='station-wrapper'>
        <div className='station-content-main'>
          <div className='station-content-container'>
            <div className='station-banner-box'>
              <div className='station-image'
                   style={{backgroundImage: `url(${station.image ? station.image : defaultStationImage})`}}
              />
            </div>

            <div className='station-main-wrapper'>
              <div className='station-content-box'>
                <div className='station-nav-group'>
                  {boardCategories.map((item) => (
                      <div
                          key={item.category}
                          className={`station-nav-list ${currentCategory === item.category ? 'active' : ''}`}
                          onClick={() => onCategoryClickHandler(item.category)}
                      >
                        {item.category}
                      </div>
                  ))}
                </div>
                <div className='divider'></div>
                <div className='station-main-content-option'>
                  <div className='station-main-content-count'>
                    총 <span className="highlight">{`${boardList.length}개`}</span>의 글
                  </div>
                  {loginUser?.userId === streamerId &&
                      <div className='station-main-post-button'
                           onClick={onPostButtonClickHandler}>{'글쓰기'}</div>
                  }
                </div>

                <div className='station-main-content'>
                  {boardList && boardList.map(board =>
                      <BoardItem key={board.boardId} boardListItem={board} user={user}
                                 onClick={() =>{
                                   onBoardDetailClickHandler(board.boardId)}}
                      />
                  )}
                </div>
              </div>

              {postView &&
                  <div className="modal-backdrop">
                    <div className="modal-container">
                      <div className="modal-body">
                        <PostBoard/>
                      </div>
                    </div>
                  </div>}

              {detailView &&
                  <div className="modal-backdrop">
                    <div className="modal-container">
                      <div className="modal-body">
                        <BoardDetail boardId={selectedBoardId} onClose={closeBoardDetailHandler}/>
                      </div>
                    </div>
                  </div>
              }

              <div className='station-user-info-box'>
                <UserBox user={user} station={station} fans={fans}/>
              </div>
            </div>
          </div>


        </div>
      </div>
  );
}