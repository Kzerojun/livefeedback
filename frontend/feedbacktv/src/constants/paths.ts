export const MAIN_PATH = (): string => '/';

export const AUTH_PATH = (): string => `/auth`;

export const MYPAGE_PATH = () : string => `/mypage`;

export const CANDY_BUY_PATH = (): string => `/candy`;

export const BROAD_CAST_PATH = (): string => `/broadcast`;

export const DONATION_PATH = (donorId: string, recipientId: string): string => `/donation/${donorId}/${recipientId}`;

export const SEARCH_PATH = (searchWord: string): string => `/search?name=${searchWord}`;

export const USER_PATH = (userId: string): string => `/user/${userId}`;

export const STATION_PATH = (streamerId: string): string => `/station/${streamerId}`;

export const STREAM_PATH = (streamerId: string, broadcastId: string | number) => `/stream/${streamerId}/${broadcastId}`;

export const PAY_PATH = (productName: string, price: number, amount: number): string => {
  const query = new URLSearchParams({
    productName: productName,
    price: price.toString(),
    amount: amount.toString()
  }).toString();
  return `/pay?${query}`;
};
