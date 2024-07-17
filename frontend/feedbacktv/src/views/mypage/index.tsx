import React, {ChangeEvent, useState} from 'react';
import {Button, Input, Space, Typography, Row, Col, message, Upload, Modal} from 'antd';
import {
  UserOutlined,
  LockOutlined,
  SafetyOutlined,
  UploadOutlined,
  LoadingOutlined,
  PlusOutlined
} from '@ant-design/icons';
import './style.css';
import {
  changeNewPassword,
  changeNickname,
  changeProfileImage,
  checkNicknameAvailability
} from "../../apis/member";
import ResponseDto from "../../types/response/response.dto";
import {
  ChangeNicknameResponseDto,
  CheckNicknameAvailabilityResponseDto
} from "../../types/response/member";
import ChangePasswordRequestDto from "../../types/request/member/change-password.request.dto";
import useLoginUserStore from "../../stores/login-user.store";
import ChangePasswordResponseDto from "../../types/response/member/change-password.response.dto";
import {useCookies} from "react-cookie";
import type {RcFile, UploadChangeParam} from 'antd/lib/upload';
import {profileImageUpload} from "../../apis/file";
import ChangeProfileImageRequestDto
  from "../../types/request/member/change-profile-image.request.dto";
import ChangeProfileImageResponseDto
  from "../../types/response/member/change-profile-image.response.dto";
import ChangeNicknameRequestDto from "../../types/request/member/change-nickname.request.dto";

const {Title} = Typography;

const nicknamePattern = /^[가-힣A-Za-z0-9!@#$%^&*()_+]{2,7}$/;
const passwordPattern = /^(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,15}$/;

export default function MyPage() {

  const [nickname, setNickname] = useState<string>('');
  const [currentPassword, setCurrentPassword] = useState<string>('');
  const [newPassword, setNewPassword] = useState<string>('');
  const [confirmPassword, setConfirmPassword] = useState<string>('');
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false);
  const [file, setFile] = useState<File | null>(null);
  const [imageUrl, setImageUrl] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const {loginUser} = useLoginUserStore();
  const [cookies, setCookie] = useCookies();

  const [nicknameError, setNicknameError] = useState<string | null>(null);
  const [passwordError, setPasswordError] = useState<string | null>(null);
  const [confirmPasswordError, setConfirmPasswordError] = useState<string | null>(null);
  const [isNicknameAvailable, setIsNicknameAvailable] = useState<boolean>(false); // 닉네임 사용 가능 여부

  const onNicknameChange = (event: ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setNickname(value);
    setIsNicknameAvailable(false); // 닉네임이 변경되면 초기화

    if (!nicknamePattern.test(value)) {
      setNicknameError('닉네임은 2-7자의 한글, 영문, 숫자 및 특수문자만 가능합니다.');
    } else {
      setNicknameError(null);
    }
  };

  const onCurrentPasswordChange = (event: ChangeEvent<HTMLInputElement>) => {
    setCurrentPassword(event.target.value);
  };

  const onNewPasswordChange = (event: ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setNewPassword(value);

    if (!passwordPattern.test(value)) {
      setPasswordError('비밀번호는 8-15자의 영문 소문자, 숫자 및 특수문자를 포함해야 합니다.');
    } else {
      setPasswordError(null);
    }
  };

  const onConfirmPasswordChange = (event: ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setConfirmPassword(value);

    if (value !== newPassword) {
      setConfirmPasswordError('비밀번호가 일치하지 않습니다.');
    } else {
      setConfirmPasswordError(null);
    }
  };

  const showModal = () => {
    setIsModalVisible(true);
  };

  const handleOk = () => {
    setIsModalVisible(false);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

  const onNicknameCheckHandler = async () => {
    if (nicknameError || nickname === '') {
      message.error('유효한 닉네임을 입력하세요.');
      return;
    }

    try {
      const response = await checkNicknameAvailability(nickname, cookies.accessToken);
      checkNicknameAvailabilityResponse(response);
    } catch (error) {
      message.error('서버 응답이 없습니다.');
    }
  };

  const checkNicknameAvailabilityResponse = (response: CheckNicknameAvailabilityResponseDto | null) => {
    if (!response) {
      message.error('서버 응답이 없습니다.');
      return;
    }

    const {code} = response;

    if (code === 'DN') {
      message.error("중복된 닉네임이 존재합니다.");
      setIsNicknameAvailable(false); // 닉네임이 중복된 경우
      return;
    }

    if (code === 'SU') {
      message.success('닉네임을 변경할 수 있습니다.');
      setIsNicknameAvailable(true); // 닉네임이 사용 가능한 경우
      return;
    }

    message.error('알 수 없는 오류가 발생했습니다.');
  };

  const onClickChangePasswordButton = () => {
    if (passwordError || confirmPasswordError || currentPassword === '') {
      message.error('유효한 비밀번호를 입력하세요.');
      return;
    }

    if (!loginUser) {
      message.error('로그인 해주세요.');
      return;
    }
    const request: ChangePasswordRequestDto = {
      currentPassword,
      newPassword,
    };

    changeNewPassword(request, cookies.accessToken).then(changePasswordResponse);
  };

  const changePasswordResponse = (response: ChangePasswordResponseDto | ResponseDto | null) => {
    if (!response) {
      message.error('서버 응답이 없습니다.');
      return;
    }

    const {code} = response as ResponseDto;

    if( code === 'PM') {
      message.error('현 비밀번호가 올바르지 않습니다.');
      return;
    }

    if (code !== 'SU') {
      message.error('알 수 없는 오류가 발생했습니다.');
      return;
    }

    message.success('비밀번호가 성공적으로 변경되었습니다.');
    window.location.reload();
  };

  const getBase64 = (file: File, callback: (url: string) => void) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => callback(reader.result as string);
    reader.onerror = (error) => {
      console.error('Error reading file:', error);
      message.error('이미지 파일을 읽는 도중 오류가 발생했습니다.');
    };
  };

  const beforeUpload = (file: RcFile) => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      message.error('JPG 또는 PNG 형식의 파일만 업로드 가능합니다!');
      return Upload.LIST_IGNORE; // 업로드 리스트에서 제거
    }
    const isLt5M = file.size / 1024 / 1024 < 5;
    if (!isLt5M) {
      message.error('이미지 파일 크기는 5MB 이하로 제한됩니다!');
      return Upload.LIST_IGNORE; // 업로드 리스트에서 제거
    }
    return isJpgOrPng && isLt5M;
  };

  const handleChange = (info: UploadChangeParam) => {
    if (info.file.status === 'removed') {
      setImageUrl(null);
      setFile(null);
      return;
    }

    if (info.file.originFileObj) {
      getBase64(info.file.originFileObj, (url) => {
        setImageUrl(url);
        setFile(info.file.originFileObj as File); // 파일 설정, 타입 확인 후 설정
      });
    }
  };

  const uploadButton = (
      <div>
        {loading ? <LoadingOutlined/> : <PlusOutlined/>}
        <div style={{marginTop: 8}}>Upload</div>
      </div>
  );

  const onUploadProfileImage = async () => {
    if (!file) {
      message.error('프로필 이미지를 선택하세요.');
      return;
    }

    if (!loginUser) {
      message.error('로그인이 필요합니다.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await profileImageUpload(formData);
      profileImageUploadResponse(response);
    } catch (error) {
      message.error('서버 응답이 없습니다.');
    }
  };

  const profileImageUploadResponse = (response: string | null) => {
    if (!response) {
      message.error('서버 응답이 없습니다.');
      return;
    }
    if (!loginUser) {
      return;
    }

    const url = response as string;
    const request: ChangeProfileImageRequestDto = {
      userLoginId: loginUser.userId,
      image: url
    };

    changeProfileImage(request, cookies.accessToken).then(changeProfileImageResponse);
  };

  const changeProfileImageResponse = (response: ChangeProfileImageResponseDto | ResponseDto | null) => {
    if (!response) {
      message.error('서버 응답이 없습니다.');
      return;
    }

    const {code} = response;

    if (code !== 'SU') {
      message.error('알 수 없는 오류가 발생했습니다.');
      return;
    }

    message.success('프로필 이미지 변경에 성공하셨습니다.');
  };

  const onClickChangeNickname = () => {
    if (nicknameError || nickname === '' || !isNicknameAvailable) {
      message.error('유효한 닉네임을 입력하세요.');
      return;
    }

    if (!loginUser) {
      message.error("로그인이 필요합니다.");
      return;
    }

    const request: ChangeNicknameRequestDto = {
      newNickname: nickname,
      accessToken: cookies.accessToken,
    };

    changeNickname(request).then(changeNicknameResponse);
  };

  const changeNicknameResponse = (response: ChangeNicknameResponseDto | ResponseDto | null) => {
    if (!response) {
      message.error("서버 응답이 없습니다.");
      return;
    }

    const {code} = response as ChangeNicknameResponseDto;

    if (code === 'DN') {
      message.error("중복된 닉네임이 존재합니다.");
      setIsNicknameAvailable(false); // 닉네임 중복 시 상태 업데이트
      return;
    }

    if (code === 'NEM') {
      message.error("회원가입이 필요합니다.");
      return;
    }

    if (code !== 'SU') {
      message.error('알 수 없는 오류가 발생했습니다.');
      return;
    }

    message.success("닉네임 변경에 성공하셨습니다.");
  };

  return (
      <div className='mypage-container'>
        {/* 닉네임 설정 */}
        <div className='section'>
          <Row align="middle">
            <Col span={4}>
              <UserOutlined style={{fontSize: '24px'}}/>
            </Col>
            <Col span={20}>
              <Title level={4}>닉네임 설정</Title>
            </Col>
          </Row>
          <Row className='content'>
            <Col span={4} className='label'>
              닉네임 설정
            </Col>
            <Col span={20}>
              <Space direction="vertical" style={{width: '100%'}}>
                <div className='nickname-input-group'>
                  <Input
                      placeholder="닉네임"
                      value={nickname}
                      onChange={onNicknameChange}
                      style={{flex: 1, marginRight: '10px'}}
                  />
                  <Button type="primary" onClick={onNicknameCheckHandler}>
                    중복확인
                  </Button>
                </div>
                {nicknameError && <div className='error-message'>{nicknameError}</div>}
                <Button onClick={onClickChangeNickname} disabled={!isNicknameAvailable}>닉네임 변경</Button>
                <div className='description'>
                  - 방송 채팅이나 게시물 작성시 보이는 닉네임입니다. (최대 7자)
                </div>
              </Space>
            </Col>
          </Row>
        </div>

        {/* 비밀번호 변경 */}
        <div className='section'>
          <Row align="middle">
            <Col span={4}>
              <LockOutlined style={{fontSize: '24px'}}/>
            </Col>
            <Col span={20}>
              <Title level={4}>비밀번호 변경</Title>
            </Col>
          </Row>
          <Row className='content'>
            <Col span={4} className='label'>
              비밀번호
            </Col>
            <Col span={20}>
              <Space direction="vertical" style={{width: '100%'}}>
                <Input.Password
                    placeholder="현재 비밀번호"
                    value={currentPassword}
                    onChange={onCurrentPasswordChange}
                    style={{width: '60%'}}
                />
                <Input.Password
                    placeholder="새 비밀번호"
                    value={newPassword}
                    onChange={onNewPasswordChange}
                    style={{width: '60%'}}
                />
                {passwordError && <div className='error-message'>{passwordError}</div>}
                <Input.Password
                    placeholder="새 비밀번호 확인"
                    value={confirmPassword}
                    onChange={onConfirmPasswordChange}
                    style={{width: '60%'}}
                />
                {confirmPasswordError &&
                    <div className='error-message'>{confirmPasswordError}</div>}
                <div className='description'>
                  비밀번호를 변경하면 현재 기기를 제외한 연결된 기기에서 로그인 계정이 모두 로그아웃 됩니다.
                </div>
                <Button type="primary" onClick={onClickChangePasswordButton}>
                  비밀번호 변경
                </Button>
              </Space>
            </Col>
          </Row>
        </div>

        {/* 프로필 이미지 변경 */}
        <div className='section'>
          <Row align="middle">
            <Col span={4}>
              <UploadOutlined style={{fontSize: '24px'}}/>
            </Col>
            <Col span={20}>
              <Title level={4}>프로필 이미지 변경</Title>
            </Col>
          </Row>
          <Row className='content'>
            <Col span={4} className='label'>
              프로필 이미지
            </Col>
            <Col span={20}>
              <Space direction="vertical" style={{width: '100%'}}>
                <Upload
                    name="profileImage"
                    listType="picture-card"
                    className="avatar-uploader"
                    showUploadList={false}
                    beforeUpload={beforeUpload}
                    onChange={handleChange}
                    style={{
                      width: '100%',
                      height: '100%',
                      display: 'flex',
                      justifyContent: 'center',
                      alignItems: 'center',
                      overflow: 'hidden'
                    }} // 이미지 컨테이너 스타일
                >
                  {imageUrl ? <img src={imageUrl} alt="avatar" style={{
                    width: '100%',
                    height: '100%',
                    objectFit: 'cover'
                  }}/> : uploadButton}
                </Upload>
                <Button type="primary" onClick={onUploadProfileImage}>
                  프로필 이미지 업로드
                </Button>
                <div className='description'>
                  - 지원되는 이미지 형식: JPG, PNG
                  <br/>
                  - 파일 크기는 최대 5MB까지 지원됩니다.
                  <br/>
                  - 420 px X 420 px
                </div>
              </Space>
            </Col>
          </Row>
        </div>

        {/* 본인 확인 */}
        <div className='section'>
          <Row align="middle">
            <Col span={4}>
              <SafetyOutlined style={{fontSize: '24px'}}/>
            </Col>
            <Col span={20}>
              <Title level={4}>본인 확인</Title>
            </Col>
          </Row>
          <Row className='content'>
            <Col span={4} className='label'>
              본인 확인
            </Col>
            <Col span={20}>
              <Button type="primary" onClick={showModal}>
                본인 인증
              </Button>
            </Col>
          </Row>
        </div>

        <Modal title="본인 인증" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel}>
          <p>본인 인증을 위한 내용을 여기에 추가하세요.</p>
        </Modal>
      </div>
  );
}
