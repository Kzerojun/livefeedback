import './style.css'
import CandyBox from "../../components/CandyBox";

export default function Buy() {
  return(

      <div className='buy-wrapper'>
        <div className='buy-grid'>
          <CandyBox productName={'사탕'} amount={100} price={100}/>
          <CandyBox productName={'사탕'} amount={500} price={100}/>
          <CandyBox productName={'사탕'} amount={1000} price={100}/>
          <CandyBox productName={'사탕'} amount={2500} price={100}/>
          <CandyBox productName={'사탕'} amount={5000} price={100}/>
          <CandyBox productName={'사탕'} amount={10000} price={100}/>
        </div>
        <div className ='buy-notice'>
        </div>
      </div>
  )
}