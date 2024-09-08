import {Button, Tabs} from 'antd'; // Ant Design의 Tabs 컴포넌트 가져오기
import {useNavigate, useParams} from "react-router-dom";
import useLoginUserStore from "../../stores/login-user.store";
import {useCookies} from "react-cookie";
import React, {useEffect, useState, useRef, ChangeEvent} from "react";
import {BoardListItem, Station, User} from "../../types/interface";
import UserBox from "../../components/UserBox";
import defaultStationImage from "assets/image/station-default-image.png";
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

const {TabPane} = Tabs; // Ant Design의 TabPane 컴포넌트 가져오기

export default function Channel() {
  const {streamerId} = useParams();
  const {loginUser} = useLoginUserStore();
  const [cookies, setCookies] = useCookies();
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
  const [currentPage, setCurrentPage] = useState<number>(0);
  const [totalBoardsSize, setTotalBoardsSize] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(false);
  const [manageView, setManageView] = useState<boolean>(false);
  const onPostButtonClickHandler = () => {
    setPostView(true);
  };

  const closeBoardDetailHandler = () => {
    setDetailView(false);
  };

  const onCloseButtonClickHandler = () => {
    setPostView(false);
    resetBoard();
  };

  const onCategoryClickHandler = (category: string) => {
    setCurrentCategory(category);
    setCurrentPage(0);
    setBoardList([]);
  };

  const onBoardDetailClickHandler = (boardId: number) => {
    setSelectedBoardId(boardId);
    setDetailView(true);
  };

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
  };

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
  };

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
  };

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
  };

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
  };

  const getBoardCategoryListResponse = (responseBody: GetBoardListResponseDto | ResponseDto | null) => {
    if (!responseBody) return;
    const {code} = responseBody;

    if (code === 'NEM') alert('존재하지 않는 유저입니다.');
    if (code === 'DBE') alert('데이터베이스 오류입니다.');

    if (code !== 'SU') {
      navigate(MAIN_PATH());
      return;
    }

    const {boardItems, totalBoardsSize} = responseBody as GetBoardListResponseDto;
    setBoardList(prevList => [...prevList, ...(boardItems || [])]);
    setTotalBoardsSize(totalBoardsSize);
  };

  const loadMoreBoards = () => {
    if (!streamerId || loading) return;
    setLoading(true);
    getBoardList(streamerId, currentCategory, currentPage).then(response => {
      getBoardCategoryListResponse(response);
      setLoading(false);
      setCurrentPage(prevPage => prevPage + 1);
    });
  };

  const onManageButtonClickHandler =() =>{
    setManageView(!manageView);
  }
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
    };

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
                <select onChange={(e) => setCategory(e.target.value)} value={category}
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
    loadMoreBoards();
  }, [currentCategory, streamerId]);

  useEffect(() => {
    console.log(boardList);  // 여기서 boardList의 상태를 출력하여 확인
  }, [boardList]);

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
                <div className='station-content'>
                  <div className='station-nav-category-bar'>
                    <Tabs
                        activeKey={currentCategory}
                        onChange={onCategoryClickHandler}
                        tabBarExtraContent={
                          <Button onClick={onManageButtonClickHandler}>
                            방송국 관리
                          </Button>
                        }
                    >
                      {boardCategories.map(item => (
                          <TabPane tab={item.category} key={item.category}>
                            <div className='station-main-content-option'>
                              <div className='station-main-content-count'>
                                총 <span className="highlight">{`${boardList.length}개`}</span>의 글
                              </div>
                              {loginUser?.userId === streamerId &&
                                  <div className='station-main-post-button'
                                       onClick={onPostButtonClickHandler}>
                                    글쓰기
                                  </div>
                              }
                            </div>
                            <div className='station-main-content'>
                              {boardList && boardList.map(board => (
                                  <BoardItem
                                      key={board.boardId}
                                      boardListItem={board}
                                      user={user}
                                      onClick={() => onBoardDetailClickHandler(board.boardId)}
                                  />
                              ))}
                            </div>
                            {boardList.length < totalBoardsSize && (
                                <div className="load-more-container">
                                  <Button onClick={loadMoreBoards} disabled={loading}
                                          type='primary'>
                                    {loading ? '로딩 중...' : '더보기'}
                                  </Button>
                                </div>
                            )}
                          </TabPane>
                      ))}
                    </Tabs>
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
                    </div>}

              </div>
              <div className='station-user-info-box'>
                <UserBox user={user} station={station} fans={fans}/>
              </div>
            </div>
          </div>
        </div>
      </div>
  );
}