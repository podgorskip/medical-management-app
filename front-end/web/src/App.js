
import './App.css';
import axios from 'axios';
import { useEffect } from 'react';
import NavBar from './layout/Navbar';

function App() {

  const requestBody = {
    "username": "mark.smith",
    "password": "mark.smith"
  }


  fetch('api/auth/authenticate', {
    headers: {
        "Content-Type": "application/json",
    }, 
    method: "post",
    body: JSON.stringify(requestBody)
})
.then((response) => {
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return response.text(); // Use response.text() to get the raw text body
})
.then((token) => {
    console.log('JWT Token:', token);
    // You can use the token as needed
})
.catch((error) => {
    console.error('Error:', error);
});

  return (
    <div className="App">
      <NavBar></NavBar>
    </div>
  );
}

export default App;
