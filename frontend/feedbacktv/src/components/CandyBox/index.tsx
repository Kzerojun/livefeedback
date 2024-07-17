import './style.css';
import {PAY_PATH} from "../../constants/paths";

interface Props {
  productName: string;
  amount: number;
  price: number;
}

export default function CandyBox(props: Props) {
  const {productName, price, amount} = props

  const onBuyButtonClickHandler = () => {
    const payUrl = PAY_PATH(productName,price,amount);
    const windowFeatures = "menubar=no,location=no,resizable=yes,scrollbars=yes,status=yes,height=380,width=420";
    window.open(payUrl, 'NewWindow', windowFeatures);
  }

  return (
      <div className='candy-card'>
        <div className='candy-image-box'>
          <div className='candy-image'/>
        </div>
        <div className='candy-info'>
          <div className='candy-top'>
            <div className='candy-name'>{productName}</div>
            <div className='candy-amount'>{amount}개</div>
          </div>
          <div className='candy-price'>
            <span className='candy-price-color'>{price}원 </span>
            (VAT 포함)
          </div>
        </div>
        <div className='buy-button' onClick={onBuyButtonClickHandler}>
          {'구매'}
        </div>
      </div>
  )
}