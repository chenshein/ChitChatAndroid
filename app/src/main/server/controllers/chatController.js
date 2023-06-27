const chatServices = require("../services/chatServices");
const tokenController = require("../controllers/tokenController");

const getChats = async (req, res) => {
  const username = await res.user.username;
   // console.log("username", username);
  const chats = await chatServices.getChats(username);
  if (!chats) {
    res.status(400).json({ message: "Can not access chats" });
  }
  res.status(200).json(chats);
};

const addChat = async (req, res) => {
  try {
    const username = await res.user.username;
    console.log("username ", username);

    if (!username) {
      res.json({ message: "Invalid token" });
    }
    const contact = await req.body.username;
    console.log("contact ", contact);

    const chat = await chatServices.addChat(username, contact);
    if (chat.message) {
      console.log("chat 400 "+chat)
      res.status(400).json(chat.message);
    }
    console.log("chat 200"+chat)
    res.status(200).json(chat);
  } catch (err) {
    res.status(500);
  }
};

const getChatsByID = async (req, res) => {
  const username = await res.user.username;
  const chatId = await chatServices.getChatsByID(username, req.params.id);
  if (!chatId) {
    return res.status(404).json({ message: "Invalid username or ID" });
  }
  res.status(200).json(chatId);
};

const deleteChat = async (req, res) => {
  const username = await res.user.username;
  const chatId = await req.params.id;
  const response = await chatServices.deleteChat(username, chatId);
  if (!response) {
    return res.status(404).json({ message: "Invalid Id or token" });
  }
  res.status(200).json("Delete succeed");
};

const addMsg = async (req, res) => {
  const username = await res.user.username;
  const chatId = await req.params.id;
  const msg = await req.body.msg;
  const response = await chatServices.addMsg(username, chatId, msg);
  if (!response) {
    return res.status(404).json({ message: "Invalid Id or token" });
  }
  res.status(200).json(response);
};
const getMsg = async (req, res) => {
  try {
    const username = await res.user.username;
    const chatId = await req.params.id;
    const response = await chatServices.getMsg(username, chatId);
    // console.log(response);
    if (!response) {
      return res.status(404).json({ message: "Invalid Id or token" });
    }
    res.status(200).json(response);
  } catch (error) {
    console.log(error);
    res.status(500);
  }
};

module.exports = {
  getChats,
  addChat,
  getChatsByID,
  deleteChat,
  addMsg,
  getMsg,
};
