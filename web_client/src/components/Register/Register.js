import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import FormControl from '@mui/material/FormControl';
import TextField from '@mui/material/TextField';
import { AccountContext } from '@/services/userService';
import FormService from "@/services/formService";
import {fields} from "./form";

export default function Register() {
  const {isLoggedIn, login} = React.useContext(AccountContext);
  const data = {};
  const [open, setOpen] = React.useState(true);
  const {onChange, submitData, error} = FormService(fields, data);
  console.log("register rerendered");
  const handleClose = () => {
    setOpen(false);
  };

  const handleSubmit = async(evt)=>{
    if(evt) evt.preventDefault();
    const data = submitData();
    if(!data) return;
    await login(data);
  }

  return (
    <>
      {isLoggedIn()?<React.Fragment/>:(
        <>
          <Dialog
            open={open}
            onClose={handleClose}
            fullWidth={true}
            maxWidth="sm"
          >
            <DialogTitle></DialogTitle>
            <DialogContent>
              <DialogContentText>
                Enter a username to continue
              </DialogContentText>
              <Box
                component="form"
                sx={{
                  display: 'flex',
                  flexDirection: 'column',
                  m: 'auto',
                  width: '100%',
                }}
                onSubmit={handleSubmit}
              >
                <FormControl sx={{ mt: 2, width: "100%" }}>
                <TextField
                    defaultValue={data.username}
                    onChange={evt=>onChange("username", evt.target.value)}
                    error={error["username"]?true:false}
                    id="username"
                    placeholder="Enter a Username"
                    variant="outlined"
                    size="small"
                    helperText={error["username"]}
                    />
                </FormControl>
              </Box>
            </DialogContent>
            <DialogActions style={{justifyContent: 'center'}}>
              <Button onClick={handleSubmit}>Submit</Button>
            </DialogActions>
          </Dialog>
        </>
      )}
    </>
  );
}