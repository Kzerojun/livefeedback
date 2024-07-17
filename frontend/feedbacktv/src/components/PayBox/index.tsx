import './style.css';
import { PaymentResponse } from "@portone/browser-sdk/v2";
import { useLocation, useNavigate } from "react-router-dom";
import {Payment, ValidatePayment} from "../../types/interface";
import {PAYMENT} from "../../constants/payment";
import {postPaymentRequest} from "../../apis/payment";

export default function PayBox() {
  const queryParams = new URLSearchParams(useLocation().search);

  const productName = queryParams.get('productName');
  const price = parseInt(queryParams.get('price') || '0', 10);

  const navigate = useNavigate();

  const onCardBoxClickHandler = () => {
  }

  const onMobileBoxClickHandler = () => {
  }

  const onNaverBoxClickHandler = () => {
  }

  const onKakaoBoxClickHandler = () => {
    if (!price || price === 0) {
      return alert("가격 정보가 올바르지 않습니다.");
    }
    if (!productName) {
      return alert("제품 이름이 지정되지 않았습니다.");
    }

    const paymentId = PAYMENT.paymentId;

    const payment: Payment = {
      paymentUid: paymentId,
      totalPrice: price,
      productName: productName
    };

    //TODO
    // prePaymentRequest(payment)
    // .then(()=>kakaoPaymentRequest(payment))
    // .then(response => kakaoPaymentResponse(response,payment));
  }

  const kakaoPaymentResponse = (response: PaymentResponse | null | undefined,payment : Payment) => {
    if (response == null) {
      alert("서버 오류입니다.");
      return;
    }

    const responseBody = response as PaymentResponse;


    if (responseBody.code != null) {
      return alert(responseBody.message);
    }

    if(responseBody.paymentId == null || !responseBody.paymentId){
      return;
    }

    const validatePayment : ValidatePayment= {
      prePaymentUid : payment.paymentUid,
      validatePaymentUid: responseBody.paymentId
    }

    postPaymentRequest(validatePayment);
  }

  return (
      <div className='pay-wrapper'>
        <div className='pay-box'>
          <div className='pay-header'>
            {'결제'}
          </div>
          <div className='pay-body'>
            <div className='pay-normal'>
              <div className='pay-normal-label'>
                {'일반 결제'}
              </div>
              <div className='pay-normal-method'>
                <div className='pay-card-box' onClick={onCardBoxClickHandler}>
                  <div className='card-icon-image-box'>
                    <div className='icon card-icon'></div>
                  </div>
                  <div>{'신용카드'}</div>
                </div>
                <div className='pay-mobile-box' onClick={onMobileBoxClickHandler}>
                  <div className='mobile-icon-image-box'>
                    <div className='icon mobile-icon'></div>
                  </div>
                  <div>{'핸드폰'}</div>
                </div>
              </div>
            </div>
            <div className='pay-easy'>
              <div className='pay-easy-label'>
                {'간편 결제'}
              </div>
              <div className='pay-easy-method'>
                <div className='pay-naver-image-box'>
                  <div className='icon naver-pay-icon'></div>
                </div>
                <div className='pay-kakao-image-box' onClick={onKakaoBoxClickHandler}>
                  <div className='icon kakao-pay-icon'></div>
                </div>
              </div>
            </div>
            <div className='pay-product-info'>
              <div className='pay-product-detail'>
                <div className='pay-product-name'>
                  {`${productName}`}
                </div>
                <div className='pay-product-price'>
                  <span className='pay-product-price-color'>{`${price}`}원 </span>
                  (VAT 포함)
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
  );
}
