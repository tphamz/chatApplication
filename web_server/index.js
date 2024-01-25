const express = require('express');
const { createServer } = require('node:http');
const cors = require('cors');

const bodyParser = require('body-parser');
const os = require('os');
const { Server } = require("socket.io");
const { createAdapter } = require("@socket.io/redis-adapter");
const { createClient } = require("redis");

const { uid } = require('uid');

const Auth = require("./auth");
const Chat = require("./chat");

const app = express();
const auth = Auth();
const server = createServer(app);

const corsOption = { origin: '*'};
app.use(cors(corsOption))


const io = new Server(server, {cors: corsOption});
const chat = Chat(io);
const port = 8000;

const errorHandler = (err, req, res, next)=>{
	console.log("/******************************/");
  console.log("/*error:");
  console.log(err);
  console.log("/******************************/");
	res.status(err.code || err.statusCode || 500);
	res.json({ error: err.message });
}

app.use(bodyParser.json());



app.get('/version', (req, res, next) => {
 res.send({version: "1.0.0", instance: "You've hit " + os.hostname(), request: "Received request from " + req.socket.localAddress});
})

app.post('/api/register', (req, res, next) => {
  const id = uid(32);
  const token = auth.token({...req.body, id});
  res.send({...req.body, id, token});
})

app.all('*', (req, res, next) => {
  throw new Error("not found");
})

app.use(errorHandler);


// const pubClient = createClient({ url: "redis://localhost:6379" });
// const subClient = pubClient.duplicate();

// Promise.all([pubClient.connect(), subClient.connect()]).then(() => {
//     io.adapter(createAdapter(pubClient, subClient));
//     io.on("connection", socket=>{
//         console.log('a user connected');
//     });
// });


server.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
});
