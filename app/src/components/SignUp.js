import { useState } from "react"
import { useAuth } from "./AuthContext"
import { Link } from 'react-router-dom'

function SignUp() {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("")
    const [isSubmitted, setIsSubmitted] = useState(false);

    const { setUser } = useAuth();


    const handleFirstNameInputChange = (event) => {
        setFirstName(event.target.value)
    }

    const handleLastNameInputChange = (event) => {
        setLastName(event.target.value)
    }

    const handleEmailInputChange = (event) => {
        setEmail(event.target.value)
    }

    const handleUsernameInputChange = (event) => {
        setUsername(event.target.value)
    }

    const handlePasswordInputChange = (event) => {
        setPassword(event.target.value)
    }

    const handlePhoneNumberInputChange = (event) => {
        setPhoneNumber(event.target.value)
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        const requestBody = {
            firstName: firstName,
            lastName: lastName,
            username: username,
            email: email,
            phoneNumber: phoneNumber,
            password: password
        };
    
        const register = async () => {

            const response = await fetch('api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestBody)
            })

            
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
               
        }

        register()
        .then(() => {
            setIsSubmitted(true);

            const authenticationRequest = {
                username: username,
                password: password
            };

            return fetch('/api/auth/authenticate', {
                method: "POST",
                headers: {"Content-Type": "application/json" },
                body: JSON.stringify(authenticationRequest)
            });
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to authenticate");
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            setUser({username, token: data.token});
        })
        .catch(error => {
            console.error('Error:', error);
        });
    };

    const handleClearButton = (event) => {
        setEmail("")
        setFirstName("")
        setLastName("")
        setUsername("")
        setPassword("")
        setPhoneNumber("")
    }
    
    return (
        <div className="registration">
        <h2 className="text-center lead display-6">Register and gain access to the best specialists</h2>
        <hr></hr>
        <form className="form">
                <label htmlFor="firstName">First name</label>
                <input type="text" onChange={handleFirstNameInputChange} placeholder="First name"/>

                <label htmlFor="lastName">Last name</label>
                <input type="text" onChange={handleLastNameInputChange} placeholder="Last name"/>

                <label htmlFor="username">Username</label>
                <input type="text" onChange={handleUsernameInputChange} placeholder="Username"/>

                <label htmlFor="email">Email</label>
                <input type="email" onChange={handleEmailInputChange} placeholder="Email"/>

                <label htmlFor="phoneNumber">Phone number</label>
                <input type="tel" onChange={handlePhoneNumberInputChange} pattern="[0-9]{3} [0-9]{3} [0-9]{3}" placeholder="Phone number"/>

                <label htmlFor="password">Password</label>
                <input type="password" onChange={handlePasswordInputChange} placeholder="Password"/>

                <div className="bttns">
                    <button type="submit" onClick={handleSubmit} className="btn btn-primary" disabled={
                        firstName.length === 0 || lastName.length === 0 || username.length === 0 || password.length === 0
                    }>Submit</button>
                    <button type="sumbit" onClick={handleClearButton} className="btn btn-primary">Clear</button>
                </div>
        </form>
        {isSubmitted && (
            <div className="thanks">
                <h5>Thank you for entrusting us with your healthcare needs.</h5>
                <h6>If you wish to receive tailored visit recommendations, we invite you to specify the particular medical conditions of interest.</h6>
                <Link to='/select-illnesses' className='link'>Set preferred illnesses</Link>
            </div>
        )

        }
        </div>
    )
}



export default SignUp;