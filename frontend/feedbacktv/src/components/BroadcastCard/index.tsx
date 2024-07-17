import React from 'react';
import './style.css';

interface StreamCardProps {
  title: string;
  description: string;
  thumbnailUrl: string;
  streamerName: string;
}

export default function BroadcastCard(props: StreamCardProps) {

  const {thumbnailUrl, title, description, streamerName} = props;

  return (
      <div className="card">
        <div className='thumbnail-box'>
          <div className='icon thumbnail'> </div>
        </div>
        <div className="content">
          <h2 className="title">{title}</h2>
          <p className="description">{description}</p>
          <p className="streamerName">{streamerName}</p>
        </div>
      </div>
  );
};
