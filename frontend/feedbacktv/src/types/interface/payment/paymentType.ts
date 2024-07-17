import {Currency, PayMethod} from "@portone/browser-sdk/dist/v2/entity";

export interface PaymentType {
  storeId: string;
  channelKey: {
    INISIS: string;
    KAKAO: string;
  };
  product: string;
  currency: Currency;
  payMethods: {
    CARD: PayMethod;
    EASY_PAY: PayMethod;
  };
  paymentId: string;
}
