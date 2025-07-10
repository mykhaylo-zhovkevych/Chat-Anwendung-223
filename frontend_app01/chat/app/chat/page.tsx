"use client";

import { useEffect, useState, useRef } from "react";
import { useRouter } from "next/navigation";
import axios from "axios";
import { Client, IMessage } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import SearchBar from "../components/SearchBar";

interface ChatMessage {
  senderName: string;
  receiverName?: string;
  message?: string;
  media?: string;
  status: "JOIN" | "LEAVE" | "MESSAGE";
}

interface User {
  username: string;
}

let stompClient: Client | null = null;

const ChatPage: React.FC = () => {
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [receiver, setReceiver] = useState("");
  const [message, setMessage] = useState("");
  const [media, setMedia] = useState("");
  const [tab, setTab] = useState("CHATROOM");
  const [publicChats, setPublicChats] = useState<ChatMessage[]>([]);
  const [privateChats, setPrivateChats] = useState<Map<string, ChatMessage[]>>(new Map());
  const router = useRouter();
  const connected = useRef(false);
  const username = (typeof window !== "undefined" ? localStorage.getItem("chat-username") : "") || "";

  useEffect(() => {
    if (!username.trim()) {
      router.push("/");
    }
  }, [username, router]);

  useEffect(() => {
  if (!connected.current) connect();

    return () => {
        stompClient?.deactivate()?.then(() => {
        console.log("Disconnected cleanly");
        }).catch((err) => {
        console.error("Error during disconnect:", err);
        });
    };
    }, []);


  const connect = () => {
    const socket = new SockJS("http://localhost:8080/ws");

    stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: onConnected,
      onStompError: (frame) => {
        console.error("STOMP Fehler: " + frame.headers["message"]);
        console.error("Details: " + frame.body);
      },
    });

    stompClient.activate();
  };

  const onConnected = () => {
    connected.current = true;

    stompClient?.subscribe("/chatroom/public", onPublicMessageReceived);
    stompClient?.subscribe(`/user/${username}/private`, onPrivateMessageReceived);

    stompClient?.publish({
      destination: "/app/message",
      body: JSON.stringify({ senderName: username, status: "JOIN" }),
    });
  };

  const onPublicMessageReceived = (payload: IMessage) => {
    const msg: ChatMessage = JSON.parse(payload.body);
    if (msg.status === "MESSAGE") {
      setPublicChats((prev) => [...prev, msg]);
    } else if (msg.status === "JOIN" && msg.senderName !== username) {
      if (!privateChats.has(msg.senderName)) {
        privateChats.set(msg.senderName, []);
        setPrivateChats(new Map(privateChats));
      }
    } else if (msg.status === "LEAVE" && msg.senderName !== username) {
      privateChats.delete(msg.senderName);
      setPrivateChats(new Map(privateChats));
    }
  };

  const onPrivateMessageReceived = (payload: IMessage) => {
    const msg: ChatMessage = JSON.parse(payload.body);
    const messages = privateChats.get(msg.senderName) || [];
    messages.push(msg);
    privateChats.set(msg.senderName, messages);
    setPrivateChats(new Map(privateChats));
  };

  const sendMessage = () => {
    if (!message.trim() && !media) return;
    const msg: ChatMessage = { senderName: username, message, media, status: "MESSAGE" };
    stompClient?.publish({
      destination: "/app/message",
      body: JSON.stringify(msg),
    });
    setMessage("");
    setMedia("");
  };

  const sendPrivate = () => {
    if (!message.trim() || !receiver) return;
    const msg: ChatMessage = {
      senderName: username,
      receiverName: receiver,
      message,
      media,
      status: "MESSAGE",
    };
    const messages = privateChats.get(receiver) || [];
    messages.push(msg);
    privateChats.set(receiver, messages);
    setPrivateChats(new Map(privateChats));
    stompClient?.publish({
      destination: "/app/private-message",
      body: JSON.stringify(msg),
    });
    setMessage("");
    setMedia("");
  };

  const handleLogout = () => {
    stompClient?.publish({
      destination: "/app/message",
      body: JSON.stringify({ senderName: username, status: "LEAVE" }),
    });
    localStorage.removeItem("chat-username");
    router.push("/");
  };

  const base64ConversionForImages = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files?.[0]) {
      const reader = new FileReader();
      reader.readAsDataURL(e.target.files[0]);
      reader.onload = () => setMedia(reader.result as string);
    }
  };

  const handlePrivateSelect = (user: User) => {
    setSelectedUser(user);
    setReceiver(user.username);
    if (!privateChats.has(user.username)) {
      privateChats.set(user.username, []);
      setPrivateChats(new Map(privateChats));
    }
  };

  const fetchChatHistory = async (other: string) => {
  try {
    const res = await axios.get(
      `http://localhost:8080/api/users/history/${username}/${other}`,
      {
        withCredentials: true,
      }
    );
    if (res.status === 200) {
      privateChats.set(other, res.data);
      setPrivateChats(new Map(privateChats));
    }
  } catch (err) {
    console.error("Fehler beim Laden des Chatverlaufs", err);
  }
};


  return (
    <div className="flex flex-col min-h-screen bg-gray-800 p-4">
      <div className="flex-grow bg-white rounded-md p-4 overflow-auto">
        {(tab === "CHATROOM" ? publicChats : privateChats.get(tab) || []).map((msg, idx) => {
          const mine = msg.senderName === username;
          return (
            <div key={idx} className={`flex ${mine ? "justify-end" : "justify-start"} my-2`}>
              <div className={`p-2 rounded ${mine ? "bg-blue-500 text-white" : "bg-gray-200"}`}>
                {!mine && <div className="font-bold">{msg.senderName}</div>}
                <div>{msg.message}</div>
                {msg.media && <img src={msg.media} alt="" className="max-h-48 mt-2 rounded" />}
              </div>
            </div>
          );
        })}
      </div>

      <div className="mt-2 flex space-x-2">
        <input
          type="text"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          className="flex-grow p-2 rounded border-1 border-gray-400"
        />
        <button
          onClick={tab === "CHATROOM" ? sendMessage : sendPrivate}
          className="flex bg-blue-600 p-2 rounded text-white"
        >
          Senden
        </button>
        <button onClick={handleLogout} className="bg-red-600 p-2 rounded text-white">
          Logout
        </button>
        <label className="bg-gray-600 p-2 rounded text-white cursor-pointer">
          📎
          <input type="file" className="hidden" onChange={base64ConversionForImages} />
        </label>
      </div>

      <SearchBar onUserSelect={handlePrivateSelect} />

      <div className="flex space-x-2 mt-2 overflow-x-auto">
        <div className="w-full text-right">
        <button
            onClick={() => setTab("CHATROOM")}
            className={`p-2 rounded ${tab === "CHATROOM" ? "bg-blue-600 text-white" : "bg-gray-300 text-black"}`}
        >
            Room Chat
        </button>
        </div>
        {[...privateChats.keys()].map((u) => (
          <button
            key={u}
            onClick={() => {
              setTab(u);
              fetchChatHistory(u);
            }}
            className={`p-2 rounded ${tab === u ? "bg-blue-400" : "bg-gray-300"}`}
          >
            {u}
          </button>
        ))}
      </div>
    </div>
  );
};

export default ChatPage;
