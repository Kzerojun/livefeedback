import React, { useState } from 'react';
import './style.css';

const categories = ['All', 'Gaming', 'Music', 'Sports', 'Education'];

interface SidebarProps {
  onSelectCategory: (category: string) => void;
}

export default function Sidebar({ onSelectCategory }: SidebarProps) {
  const [activeCategory, setActiveCategory] = useState<string>('All');

  const handleCategoryClick = (category: string) => {
    setActiveCategory(category);
    onSelectCategory(category);
  };

  return (
      <div className="sidebar">
        <div className="sidebar-title">{'카테고리'}</div>
        <div className="category-list">
          {categories.map((category, index) => (
              <div
                  key={index}
                  onClick={() => handleCategoryClick(category)}
                  className={`category-item ${activeCategory === category ? 'active' : ''}`}
              >
                {category}
              </div>
          ))}
        </div>
      </div>
  );
};
