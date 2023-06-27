const Chat = require("../models/chat");
const userServices = require("../services/userServices");
const User = require("../models/user");
const Message = require("../models/message");

const getChats = async (username) => {
  const user = await userServices.getUser(username);
  if (!user) {
    // console.log("User not found");
    return;
  }
  // console.log("user2", user);
  // const chats = user.chats;
  const chats = await Chat.find({
    $or: [
      { "users.0.username": user.username },
      { "users.1.username": user.username },
    ],
  });
  if (!chats[0]) {
    return [];
  }
  // const finalChats = []
  // finalChats.push(await chats[0].users)
  // console.log("CHATS USER TEST " + chats[0].users)
  // if (!chats) {
  //   return;
  // }
  // console.log("CHATS AER - " +chats[0].users)
  const updatedUsersArray = [];
  for (let i = 0; i < chats.length; i++) {
    const lastMessage = chats[i].lastMessage;
    let createTime;
    let messId;
    if(lastMessage){
      createTime = chats[i].messages[0].timestamp
      messId = chats[i].messages[0].id
    }
    //
    // console.log("i ",i)
    // console.log("createTime ",createTime)
    const updatedUsers = chats[i].users.map((user) => ({
      id: chats[i].id,
      user: {
        username: user.username,
        displayName: user.displayName,
        profilePic: user.profilePic,
      },
      lastMessage: lastMessage !== null
          ? {
            id: messId,
            created: createTime,
            content: lastMessage
          }
          : null
    }));
    // return the values of the map
    // const chatsArray = Array.from(chats.values());
    // const contactChat = chats[0].find(user => user.username === username);
    // console.log("Chat return", chats[0]);
    const returnChat = updatedUsers.find(
        (contact) => contact.user.username !== username
    );
    updatedUsersArray.push(returnChat);
  }
  return updatedUsersArray;
};

const addChat = async (curUsername, contactUsername) => {
  try {
    const curUser = await User.findOne({ username: curUsername });
    const contact = await User.findOne({ username: contactUsername });
    if (!curUser || !contact) {
      return { message: "Cannot find contact" };
    }
    if (curUser.username === contact.username) {
      return { message: "You can't have a conversation with yourself" };
    }
    if (!contact) {
      return { message: "Contact not found" };
    }

    // Check if the chat already exists
    // const existingChat = curUser.chats.find(
    //   (chat) => chat.user.username === contactUsername
    // );
    // if (existingChat) {
    //   return { message: "Chat already exists" };
    // }

    // Create the new chat
    const newChat = new Chat({
      users: [contact, curUser],
    });

    // curUser.chats.push(newChat);
    await newChat.save();
    let userContact = {
      username: contact.username,
      displayName: contact.displayName,
      profilePic: contact.profilePic,
    };
    let resContact = { id: newChat._id, user: userContact };
    return resContact;
  } catch (error) {
    console.log(error);
    return { message: error };
  }
};

const getChatsByID = async (username, chatId) => {
  try {
    const user = await userServices.getUser(username);
    if (!user) {
      // console.log("User not found");
      return;
    }
    const chat = await Chat.findOne({ _id: chatId });

    // const chat = user.chats.find((chats) => (chats._id = chatId));

    // const usersArray = [
    //   {
    //     username: user.username,
    //     displayName: user.displayName,
    //     profilePic: user.profilePic,
    //   },
    //   {
    //     username: chat.user.username,
    //     displayName: chat.user.displayName,
    //     profilePic: chat.user.profilePic,
    //   },
    // ];

    const messagesArray = chat.messages.map((message) => {
      const sender = {
        username: message.sender.username,
        displayName: message.sender.displayName,
        profilePic: message.sender.profilePic,
      };

      return {
        id: message._id,
        created: message.createdAt,
        sender: sender,
        content: message.content,
      };
    });
    // console.log("test ", messagesArray)

    return {
      id: chatId,
      users: chat.users,
      messages: chat.messages !== null ? messagesArray : []
    };
  } catch (error) {
    console.log(error);
    return { message: error };
  }
};

const addMsg = async (username, chatId, msg) => {
  // console.log("the msg is ",msg)
  try {
    const user = await userServices.getUser(username);
    if (!user) {
      // console.log("User not found");
      return;
    }
    const chat = await Chat.findOne({ _id: chatId });
    // console.log(chat);
    if (chat.length === 0) {
      return null; //wrong id
    }

    const message = new Message({
      sender: {
        username: user.username,
        displayName: user.displayName,
        profilePic: user.profilePic,
      },
      timestamp: Date.now(),
      content: msg,
    });
    // const data = {
    //   sender: {
    //     username: user.username,
    //     displayName: user.displayName,
    //     profilePic: user.profilePic,
    //   },
    //   content: msg,
    // };
    // console.log(await chat[0].messages);
    await chat.messages.push(message);
    // sort chat.messages by timestamp
    chat.messages.sort((a, b) => {
      return a.timestamp - b.timestamp;
    });
    await chat.messages.reverse();
    chat.lastMessage = msg;
    await chat.save();

    // const size = messMap.length - 1;
    // return messMap[size];
    // return {
    //   id: msg._id,
    //   created: Date.now(),
    //   sender: {
    //     username: user.username,
    //     displayName: user.displayName,
    //     profilePic: user.profilePic,
    //   },
    //   content: msg
    // };
    const returnRes = {
      id: message.id,
      created: message.timestamp,
      sender: message.sender,
      content: message.content,
    };
    return returnRes;
  } catch (error) {
    console.log(error);
    return { message: error };
  }
};

const getMsg = async (username, chatId) => {
  try {
    const user = await userServices.getUser(username);
    if (!user) {
      // console.log("User not found");
      return;
    }
    const chat = await Chat.findOne({ _id: chatId });

    if (chat && chat.length === 0) {
      return null; //wrong id
    }
    try {
      return (
          chat &&
          chat.messages &&
          chat.messages.map((message) => {
            return {
              id: message._id,
              created: message.timestamp,
              sender: message.sender,
              content: message.content,
            };
          })
      );
    } catch (error) {
      console.log(error);
      return { message: error };
    }
  } catch (error) {
    console.log(error);
    return { message: error };
  }
};

const deleteChat = async (username, chatId) => {
  try {
    const user = await userServices.getUser(username);
    if (!user) {
      // console.log("User not found");
      return null;
    }
    const chat = await Chat.findOne({ _id: chatId });
    if (!chat) {
      return null;
    }
    await chat.deleteOne();
    return user;
  } catch (error) {
    console.log(error);
    return { message: error };
  }
};
module.exports = {
  getChats,
  addChat,
  getChatsByID,

  addMsg,
  getMsg,
  deleteChat,
};
