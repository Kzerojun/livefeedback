import axiosInstance from './common/axiosConfig';
import { API_URL } from './constants/api';
import { Payment, ValidatePayment } from '../types/interface';
import * as PortOne from '@portone/browser-sdk/v2';
import {PAYMENT} from "../constants/payment";

export const prePaymentRequest = async (payment: Payment): Promise<string | null> => {
  try {
    const response = await axiosInstance.post(API_URL.PAYMENT_PREPARE, payment);
    return response.data;
  } catch (error) {
    return null;
  }
};

export const postPaymentRequest = async (validatePayment: ValidatePayment): Promise<string | null> => {
  try {
    const response = await axiosInstance.post(API_URL.PAYMENT_VALIDATE, validatePayment);
    return response.data;
  } catch (error) {
    return null;
  }
};

export const cardPaymentRequest = async (price: number, memberPaymentInfo: any): Promise<any> => {
  try {
    const response = await PortOne.requestPayment({
      storeId: PAYMENT.storeId,
      channelKey: PAYMENT.channelKey.INISIS,
      paymentId: PAYMENT.paymentId,
      orderName: PAYMENT.product,
      totalAmount: price,
      currency: PAYMENT.currency,
      payMethod: PAYMENT.payMethods.CARD,
      customer: {
        fullName: memberPaymentInfo.fullName,
        phoneNumber: memberPaymentInfo.phoneNumber,
        email: memberPaymentInfo.phoneNumber,
      },
    });
    return response;
  } catch (error) {
    return null;
  }
};

export const kakaoPaymentRequest = async (payment: Payment): Promise<any> => {
  try {
    const response = await PortOne.requestPayment({
      storeId: PAYMENT.storeId,
      channelKey: PAYMENT.channelKey.KAKAO,
      paymentId: payment.paymentUid,
      orderName: PAYMENT.product,
      totalAmount: payment.totalPrice,
      currency: PAYMENT.currency,
      payMethod: PAYMENT.payMethods.EASY_PAY,
    });
    return response;
  } catch (error) {
    return null;
  }
};
