const jwt = require("jsonwebtoken");
const { getUser } = require("../services/userServices");

/*
Token Creation and Verification
 */
const Tokens = async (req, res) => {
  try {
    const { username, password } = await req.body;
    const user = await getUser(username);
    if (user && user.password === password) {
      let accessToken = generateAccessToken(user);
      accessToken = accessToken.replace(/^"(.*)"$/, "$1");
       //console.log("TOKEN- " + accessToken);
      res.status(200).json(accessToken);
    } else {

      res.status(401).json({ message: "Invalid username or password" });
    }
  } catch (error) {
    console.log(error);
  }
};

const generateAccessToken = (user) => {
  return jwt.sign(
      { username: user.username },
      process.env.ACCESS_TOKEN_SECRET,
      {
        expiresIn: "1h",
      }
  );
};

const AuthenticateToken = async (req, res, next) => {
  const authHeader = await req.headers["authorization"];
  let token = authHeader && authHeader.split(" ")[1];

  if (!token) {
    return res.sendStatus(401);
  }
  token = token.replace(/^"(.*)"$/, "$1");
  jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, user) => {
    if (err) {
      // console.log(token);
      return res.status(403).json({ message: "Invalid token" });
    }
    res.user = user;
    next();
  });
};

module.exports = {
  Tokens,
  AuthenticateToken,
};
