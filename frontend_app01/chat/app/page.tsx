"use client";

import { useEffect, useState, useRef } from "react";
import { useRouter } from "next/navigation";
import SockJS from "sockjs-client";
import { Client, IMessage as Message } from "@stomp/stompjs";
import SearchBar from "./components/SearchBar";
import axios from "axios";
import SignupForm from "./components/SignupForm";


const Login: React.FC = () => {
  const router = useRouter();
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [error, setError] = useState<string>("");
  const [isSignup, setIsSignup] = useState<boolean>(false);

  useEffect(() => {
    const storedUsername = localStorage.getItem("chat-username");
    if (storedUsername) {
      router.push("/chat");
    }
  }, [router]);

  const handleLogin = async () => {
    try {
      setError("");
      if (!username || !password) {
        setError("Please enter username and password.");
        return;
      }
      const response = await axios.post(
        "http://localhost:8080/api/users/login",
        { username, password },
        { withCredentials: true }
      );
      if (response.status === 200) {
        localStorage.setItem("chat-username", username);
        router.push("/chat");
      }
    } catch (error: any) {
      console.error("Error logging in:", error);
      if (axios.isAxiosError(error)) {
        if (error.response?.status === 401) {
          setError("Invalid username or password.");
        } else if (error.response?.status === 404) {
          setError("User not found.");
        } else {
          setError("An error occurred. Please try again.");
        }
      } else {
        setError("Unexpected error occurred.");
      }
    }
  };

  const handleJwtLogin = async () => {
  try {
    setError("");
    if (!username || !password) {
      setError("Please enter username and password.");
      return;
    }

    const response = await axios.post("http://localhost:8080/api/users/jwt-login", {
      username,
      password,
    });

    if (response.status === 200 && response.data.token) {
      // Speichere Token + Username
      localStorage.setItem("jwt-token", response.data.token);
      localStorage.setItem("chat-username", username);

      router.push("/chat");
    } else {
      setError("Login failed. No token received.");
    }
  } catch (error: any) {
    console.error("JWT Login error:", error);
    if (axios.isAxiosError(error)) {
      if (error.response?.status === 401) {
        setError("Invalid username or password.");
        } else if (error.response?.status === 404) {
          setError("User not found.");
        } else {
          setError("JWT login failed. Try again.");
        }
      } else {
        setError("Unexpected error during JWT login.");
      }
    }
  };


  const handleSignupSuccess = (newUsername: string) => {
    localStorage.setItem("chat-username", newUsername);
    router.push("/chat");
  };

  const handleKeyUp = (e: React.KeyboardEvent<HTMLInputElement>) => {
  if (e.key === "Enter") {
      handleLogin();
    }
  };

  return (
  <div className="flex items-center justify-center min-h-screen bg-gradient-to-tr from-gray-800 via-gray-700 to-gray-900 px-4">
    <div className="w-full max-w-md bg-white bg-opacity-90 rounded-lg shadow-lg p-8">
      {error && (
        <div className="mb-4 rounded border border-red-400 bg-red-100 text-red-700 font-semibold p-3 text-center">
          {error}
        </div>
      )}

      {isSignup ? (
        <SignupForm onSignupSuccess={handleSignupSuccess} />
      ) : (
        <>
          <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">Login</h2>
          <form onSubmit={(e) => e.preventDefault()} className="flex flex-col gap-4">
            <input
              type="text"
              className="rounded-md border border-gray-300 p-3 w-full text-center text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              onKeyUp={handleKeyUp}
              required
              autoComplete="username"
            />
            <input
              type="password"
              className="rounded-md border border-gray-300 p-3 w-full text-center text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              onKeyUp={handleKeyUp}
              required
              autoComplete="current-password"
            />
            <button
              type="button"
              className="bg-blue-600 text-white rounded-md py-3 mt-2 font-semibold shadow hover:bg-blue-700 transition"
              onClick={handleLogin}
            >
              Connect
            </button>

            <button
            type="button"
            className="bg-green-600 text-white rounded-md py-3 mt-2 font-semibold shadow hover:bg-green-700 transition"
            onClick={handleJwtLogin}
          >
            Login with JWT
          </button>


            <button
              type="button"
              className="mt-6 text-blue-600 underline text-center hover:text-blue-800 transition"
              onClick={() => setIsSignup(true)}
            >
              Don&apos;t have an account? Sign up
            </button>
          </form>
        </>
      )}
    </div>
  </div>
);
};

export default Login;