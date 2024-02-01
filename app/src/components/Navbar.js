import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';


function Navbar() {
    const [specializations, setSpecializations] = useState([]);
    const [illnesses, setIllnesses] = useState([]);

    useEffect(() => {
       fetch('/api/auth/specializations', {method: 'GET'})
        .then(response => response.json())
        .then(data => setSpecializations(data))
        .catch(error => console.error('Error fetching specializations:', error));
    }, []); 

    useEffect(() => {
        fetch("/api/auth/illnesses", {method: "GET"})
        .then(response => response.json())
        .then(data => setIllnesses(data))
        .catch(error => console.error('Error fetching illnesses:', error));
    }, [])

    return (
        <nav>
            <ul className='navbar-m'>
                <div>
                    <li className='nav-item'>
                        <Link to="/" className='nav-link '>Home</Link>
                    </li>
                    <li className='nav-item'>
                        <Link to='/specializations' className='nav-link'>Specializations</Link>
                    </li>
                    <li className='nav-item'>
                        <Link to='/doctors' className='nav-link'>Doctors</Link>
                    </li>
                </div>
                 <div>
                     <li className='nav-item'>
                         <Link to='/my-account' id='my-account' className='nav-link disabled-link'>Account</Link>
                     </li>
                    <li className='nav-item'>
                        <Link to="/signin" className='nav-link'>Sign in</Link>
                    </li>
                    <li className='nav-item'>
                        <Link to="/signup" className='nav-link'>Sign up</Link>
                    </li>
                 </div>
            </ul>
        </nav>
    )
}

function capitalizeFirstLetter(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }

export default Navbar;