import { useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from './AuthContext';

function SignIn() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [signedIn, setSignedIn] = useState(false);

    const { setUser } = useAuth();

    const handleUsernameInputChange = (e) => {
        setUsername(e.target.value);
    }

    const handlePasswordInputChange = (e) => {
        setPassword(e.target.value);
    }

    const handleClearButton = () => {
        setPassword("");
        setUsername("");
    }

    const handleSubmitButton = async (event) => {
        event.preventDefault();
        const credentials = {username: username, password: password};

        fetch('/api/auth/authenticate', {
            method: "POST", 
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(credentials),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(token => {
            setSignedIn(true)
            console.log(token.token)
            setUser({ username, token: token.token });
        });

        const accountButton = document.getElementById("my-account");
        accountButton.classList.remove("disabled-link");
    }

    return (
        <div className="sign-in">
            <h2 className="text-center my-3 lead display-6">Sign in to access visit scheduling and other</h2>
            <hr></hr>
            <form className="form">
                <label htmlFor="username">Username</label>
                <input type="text" onChange={handleUsernameInputChange} placeholder="Username"></input>

                <label htmlFor="password">Password</label>
                <input type="password" onChange={handlePasswordInputChange} placeholder="Password"></input>

                <div className="bttns">
                    <button className="btn btn-primary" onClick={handleSubmitButton} disabled={username.length === 0 || password.length === 0}>Submit</button>
                    <button className="btn btn-primary" onClick={handleClearButton}>Clear</button>
                </div>

                {signedIn && (
                    <div class="alert alert-success" role="alert">
                        <h4 class="alert-heading">Well done!</h4>
                        <p>You've successfully signed in.</p>
                        <hr/>
                        <p class="mb-0">Check out your account details</p>
                        <Link to="/my-account">Account</Link>
                  </div>
                    
                )}

            </form>
        </div>
    )
}

export default SignIn;