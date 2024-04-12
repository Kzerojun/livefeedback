export const MAIN_PATH = () => '/';

export const AUTH_PATH = () => `/auth`;

export const SEARCH_PATH = (searchWord: string) => `/search?name=${searchWord}`;

export const USER_PATH = (userLoginId: string) => `/user/${userLoginId}`;

export const STATION_PATH = (userLoginId: string) => `/station/${userLoginId}`;
