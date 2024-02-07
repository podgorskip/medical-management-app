import { useEffect, useState } from 'react';
import { useAuth } from './AuthContext';
import profilePicture from '../images/user.png';

function Settings() {
    const {user: authenticatedUser} = useAuth();
    const [user, setUser] = useState({});
    const [isUpdateCredentialsClicked, setIsUpdateCredentialsClicked] = useState(false);
    const [isUpdatePasswordClicked, setIsUpdatePasswordClicked] = useState(false);
    const [response, setResponse] = useState({});

    const [updatedCredentials, setUpdatedCredentials] = useState({
        username: "",
        firstName: "",
        lastName: "",
        email: "",
        phoneNumber: ""
    });

    const [updatedPassword, setUpdatedPassword] = useState({
        oldPassword: "",
        newPassword: ""
    });

    useEffect(() => {
        const fetchAccountDetails = async () => {
            const response = await fetch('/api/details', {
                method: "GET",
                headers: {"Authorization": "Bearer " + authenticatedUser.token}
            });

            const data = await response.json();

            setUser(data);
        };

        fetchAccountDetails();
    }, []);

    const handleUpdateCredentialsClicked = () => {
        setIsUpdateCredentialsClicked(true);
        setIsUpdatePasswordClicked(false);
        response.httpStatus = "";
    }

    const handleUpdatePasswordClicked = () => {
        setIsUpdateCredentialsClicked(false);
        setIsUpdatePasswordClicked(true);
        response.httpStatus = "";
    }

    const handleCredentialsInputChange = (e) => {
        const {id, value} = e.target;
        setUpdatedCredentials((prevCredentials) => ({
            ...prevCredentials, [id]: value
        }));
    }

    const handlePasswordInputChange = (e) => {
        const {id, value} = e.target;
        setUpdatedPassword((prevCredentials) => ({
            ...prevCredentials, [id]: value
        }));
    }

    const handleUpdateCredentialsSubmit = (e) => {
        e.preventDefault();
        
        const updateCredentials = async () => {
            console.log(updatedPassword)
            const response = await fetch('/api/change-credentials', {
                method: "PATCH",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(updatedCredentials)
            });
            
            const data = await response.json();
            setResponse(data);
        };

        updateCredentials();
    }

    const handleUpdatePasswordSubmit = (e) => {
        e.preventDefault();

        const updatePassword = async () => {
            const response = await fetch('/api/change-password', {
                method: "PATCH",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(updatedPassword)
            });

            const data = await response.json();
            setResponse(data);
        };

        updatePassword();
    }

    const clearObject = (e) => {
        e.preventDefault();

        setUpdatedCredentials({
            username: "",
            firstName: "",
            lastName: "",
            email: "",
            phoneNumber: ""
        });
    
        setUpdatedPassword({
            oldPassword: "",
            newPassword: ""
        });
    }



    return (
        <div className='settings'>
            <img src={profilePicture} width={200} height={200}></img>
            <div>
                <h3>Account information</h3>
                <div className='credentials'>
                    <p>Full name</p><p>{user.fullName}</p>
                    <p>Username</p><p>{authenticatedUser.username}</p>
                    <p>Email</p><p>{user.email}</p>
                    <p>Phone number</p><p>{user.phoneNumber}</p>
                </div>
            </div>
            <div className='bttns'>
                <button className='btn btn-primary' onClick={handleUpdateCredentialsClicked}>Update credentials</button>
                <button className='btn btn-primary' onClick={handleUpdatePasswordClicked}>Update password</button>
            </div>
            {isUpdateCredentialsClicked && (
                <div>
                    <form className='form'>
                        <label htmlFor='firstName'>First name</label>
                        <input type='text' id='firstName' value={updatedCredentials.firstName} onChange={handleCredentialsInputChange}></input>

                        <label htmlFor='lastName'>Last name</label>
                        <input type='text' id='lastName' value={updatedCredentials.lastName} onChange={handleCredentialsInputChange}></input>

                        <label htmlFor='username'>Username</label>
                        <input type='text' id='username' value={updatedCredentials.username} onChange={handleCredentialsInputChange}></input>

                        <label htmlFor='email'>Email</label>
                        <input type='email' id='email' value={updatedCredentials.email} onChange={handleCredentialsInputChange}></input>

                        <label htmlFor='phoneNumber'>Phone number</label>
                        <input type='tel' id='phoneNumber' pattern='[0-9]{3} [0-9]{3} [0-9]{3}' value={updatedCredentials.phoneNumber} onChange={handleCredentialsInputChange}></input>

                        <div className='bttns'>
                            <button className='btn btn-primary' onClick={handleUpdateCredentialsSubmit}>Submit</button>
                            <button className='btn btn-primary' onClick={clearObject}>Clear</button>
                        </div>

                        {response.httpStatus === 'OK' && (
                            <div class="alert alert-success" role="alert">
                                {response.message}
                            </div>
                        )}
                        {response.httpStatus === 'CONFLICT' && (
                            <div class="alert alert-danger" role="alert">
                                {response.message}
                            </div>                      
                        )}
                    </form>
                </div>
            )}
            {isUpdatePasswordClicked && (
                <div>
                    <form className='form'>
                    <label htmlFor='oldPassword'>Old password</label>
                        <input type='password' id='oldPassword' value={updatedPassword.oldPassword} onChange={handlePasswordInputChange}></input>

                        <label htmlFor='newPassword'>New password</label>
                        <input type='password' id='newPassword' value={updatedPassword.newPassword} onChange={handlePasswordInputChange}></input>

                        <div className='bttns'>
                            <button className='btn btn-primary' onClick={handleUpdatePasswordSubmit}>Submit</button>
                            <button className='btn btn-primary' onClick={clearObject}>Clear</button>
                        </div>

                        {response.httpStatus === 'OK' && (
                            <div class="alert alert-success" role="alert">
                                {response.message}
                            </div>
                        )}
                        {response.httpStatus === 'CONFLICT' && (
                            <div class="alert alert-danger" role="alert">
                                {response.message}
                            </div>                      
                        )}
                    </form>
                </div>
            )}
        </div>
    )
}

export default Settings;