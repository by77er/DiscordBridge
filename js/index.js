const {Socket} = require('net')
const {Client} = require('discord.js');

const port = 45376;
const channelname = "general";

const client = new Client();
const sock = new Socket();

function retryConnection() {
  console.log('[!] Connection failed; retrying...');
  setTimeout(() => {
    sock.connect(port, 'localhost', () => {
      console.log('[*] Connected to chat plugin!');
    });
  }, 10000); // on error, reconnect
}

sock.on('error', console.error);
sock.on('close', retryConnection);

client.on('ready', () => {
  console.log(`[*] Logged in as ${client.user.tag}!`);
  console.log(`[-] Connecting to chat plugin at localhost:${port}...`);
  sock.connect(port, 'localhost', () => {
    console.log('[*] Connected to chat plugin!');
  });
});

client.on('message', msg => {
  if (msg.channel.name == channelname) {
    msg.guild.fetchMember(msg.author)
      .then((gm) => {
        sock.write(`${gm.displayName}\n${msg.cleanContent}`);
      }).catch(console.error);
  }
});


if (!process.env.DSBTOKEN) {
    console.log('Failed to start - Please set the $DSBTOKEN env variable!');
} else {
    client.login(process.env.DSBTOKEN);
}