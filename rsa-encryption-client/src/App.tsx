import './App.css'
import {Box, Button, Container, TextField, Typography} from "@mui/material";
import {makeStyles} from 'tss-react/mui';
import {useEffect, useState} from "react";
import {generateRSAKeys, process} from "./rsaService";

const useStyles = makeStyles()((theme) => {
  return {
    wrapper: {
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      height: "100vh",
    },
    content: {
      display: "flex",
      flexDirection: "column",
      gap: theme.spacing(3),
      minWidth: "300px",
    },
  }
});

function App() {

  const {classes} = useStyles();

  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const handleClick = async () => {
    await process({
      login,
      password,
      callback: setMessage
    })
  };

  useEffect(() => {
    generateRSAKeys();
    console.log("on mount")
  }, []);

  return (
    <Container className={classes.wrapper}>
      <Box className={classes.content}>
        <Typography>
          RSA Encryption Login
        </Typography>
        <TextField
          placeholder='Your login here...'
          value={login}
          onChange={(e) => setLogin(e.target.value)}
        />
        <TextField
          placeholder='and gimme ur pass here...'
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          type='password'
        />
        <Button
          variant="contained"
          color="primary"
          onClick={handleClick}
        >
          <Typography>
            Login
          </Typography>
        </Button>
        <Typography>
          Here is a message for ya...
        </Typography>
        <TextField
          placeholder='looooong msg'
          value={message}
        />
      </Box>
    </Container>
  )
}

export default App
