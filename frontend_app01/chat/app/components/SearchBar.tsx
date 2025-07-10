import { useState, ChangeEvent } from "react";
import axios from "axios";

interface User {
  id: string;
  username: string;
}

interface SearchBarProps {
  onUserSelect: (user: User) => void;
}

const SearchBar: React.FC<SearchBarProps> = ({ onUserSelect }) => {
  const [username, setUsername] = useState<string>("");
  const [searchResults, setSearchResults] = useState<User[]>([]);
  const [error, setError] = useState<string | null>(null);

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    setUsername(e.target.value);
  };

  const handleSearch = async () => {
    try {
      setError(null);
      const response = await axios.get(
    `http://localhost:8080/api/users/search?username=${username}`
  );

      setSearchResults(response.data ? [response.data] : []);
    } catch (error) {
      console.error("Error searching for user:", error);
      if (axios.isAxiosError(error) && error.response && error.response.status === 404) {
        setError("User not found.");
      } else {
        setError("An error occurred while searching.");
      }
      setSearchResults([]);
    }
  };

  const handleUserSelect = (user: User) => {
    setUsername("");
    setSearchResults([]);
    onUserSelect(user);
  };

  return (
    <div className="relative w-64">
      <input
        type="text"
        className="w-full px-4 py-2 border border-gray-400 rounded-lg focus:outline-none focus:border-blue-600"
        placeholder="Search username..."
        value={username}
        onChange={handleInputChange}
      />
      <button
        className="absolute right-0 top-0 mt-2 mr-2"
        onClick={handleSearch}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className="h-6 w-6 text-blue-700"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M15 11a4 4 0 11-8 0 4 4 0 018 0z"
          />
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M17.5 17.5l4.5 4.5"
          />
        </svg>
      </button>
      {error && <p className="text-red-500 mt-2">{error}</p>}
      {searchResults.length > 0 && (
        <div className="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-lg shadow-lg">
          {searchResults.map((user) => (
            <div
              key={user.id}
              className="cursor-pointer p-2 hover:bg-gray-100"
              onClick={() => handleUserSelect(user)}
            >
              {user.username}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default SearchBar;