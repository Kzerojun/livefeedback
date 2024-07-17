import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const SOCKET_URL = 'http://localhost:8080/ws';

class WebSocketService {
  private client: Client;
  private subscriptions: Map<string, any> = new Map();

  constructor() {
    const socket = new SockJS(SOCKET_URL);
    this.client = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
      onConnect: () => this.onConnect(),
      onStompError: (frame) => this.onError(frame),
    });
  }

  activate() {
    this.client.activate();
  }

  deactivate() {
    this.client.deactivate();
  }

  private onConnect() {
    console.log('Connected to WebSocket server');
    this.subscriptions.forEach((callback, destination) => {
      this.client.subscribe(destination, callback);
    });
  }

  private onError(frame: any) {
    console.error('WebSocket error: ' + frame.headers['message']);
    console.error('Details: ' + frame.body);
  }

  isConnected() {
    return this.client.connected;
  }

  subscribe(destination: string, callback: (message: IMessage) => void) {
    if (this.client.connected) {
      this.client.subscribe(destination, callback);
    }
    this.subscriptions.set(destination, callback);
  }

  unsubscribe(destination: string) {
    if (this.client.connected) {
      this.client.unsubscribe(destination);
    }
    this.subscriptions.delete(destination);
  }

  sendMessage(destination: string, body: any) {
    if (this.client.connected) {
      this.client.publish({
        destination,
        body: JSON.stringify(body),
      });
    } else {
      console.error('WebSocket is not connected.');
    }
  }
}

const webSocketService = new WebSocketService();
export default webSocketService;
