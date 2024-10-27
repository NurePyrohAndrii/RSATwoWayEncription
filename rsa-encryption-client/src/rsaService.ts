import NodeRSA from "node-rsa";

const getServerPublicKeyUrl = "http://localhost:8080/api/login/init";
const loginUrl = "http://localhost:8080/api/login";

let publicKey : string;
let privateKey : string;

interface ProcessProps {
  login: string;
  password: string;
  callback: (message: string) => void;
}

export const process = async (props: ProcessProps) => {
  const {
    login,
    password,
    callback
  } = props;

  const getServerPublicKeyResult = await fetch(getServerPublicKeyUrl);
  const getServerPublicKeyJson = await getServerPublicKeyResult.json();
  const serverPublicKey = getServerPublicKeyJson.publicKey;

  const credentials = {
    login: login,
    password: password
  }

  const dataString = JSON.stringify(credentials);

  const encryptor = new NodeRSA(serverPublicKey, 'public', { encryptionScheme: 'pkcs1' });
  const encryptedData = encryptor.encrypt(dataString, 'base64');

  const loginRequest = {
    base64EncodedEncryptedData: encryptedData,
    rsaPublicKey: publicKey
  }

  const loginRequestStr = JSON.stringify(loginRequest);

  const loginResult = await fetch(loginUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: loginRequestStr
  });

  const messageResponse = await loginResult.json();
  const encryptedMessage = messageResponse.base64EncodedEncryptedData;

  const decryptor = new NodeRSA(privateKey, 'private', { encryptionScheme: 'pkcs1' });
  const decryptedMessage = decryptor.decrypt(encryptedMessage, 'utf8');

  callback(decryptedMessage);
}

export const generateRSAKeys = (length: number = 2048) => {
  const key = new NodeRSA({b: length});
  publicKey = key.exportKey('public');
  privateKey = key.exportKey('private')

  publicKey = publicKey.replace(/(\r\n|\n|\r)/gm, "");
  privateKey = privateKey.replace(/(\r\n|\n|\r)/gm, "");
}