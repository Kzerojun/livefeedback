import {PaymentType} from "../types/interface/payment/paymentType";

export const PAYMENT: PaymentType = {
  storeId: "store-fab563c5-1908-446b-a082-6abc3f087513",
  channelKey: {
    INISIS: 'channel-key-a2e91518-b478-43de-909b-3585fe9b0b2a',
    KAKAO: 'channel-key-6c38dd42-0977-4d94-91e1-b2fd394307a0'
  },
  product: "별사탕",
  currency: "CURRENCY_KRW", // 문자열 리터럴 타입으로 설정
  payMethods: {
    CARD: "CARD" as "CARD",
    EASY_PAY: "EASY_PAY" as "EASY_PAY",
  },
  paymentId: `payment${crypto.randomUUID()}`,
};
