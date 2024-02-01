import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "./AuthContext";

function Account() {

    const {user: authenticatedUser} = useAuth();
    const [user, setUser] = useState({});
    const [availableVisits, setAvailableVisits] = useState([]);

    useEffect(() => {
        fetch('/api/details', {
          method: "GET",
          headers: {"Authorization": "Bearer " + authenticatedUser.token}
        })
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then(data => setUser(data))
        .catch(error => console.error('Error fetching user details:', error));
      }, []); 

      useEffect(() => {
        const fetchScheduledVisits = async () => {
            try {
                const response = await fetch('/api/patient/scheduled-visits', {
                    method: "GET",
                    headers: {"Authorization": "Bearer " + authenticatedUser.token}
                });
    
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
    
                const data = await response.json();
                setAvailableVisits(data);
            } catch (error) {
                console.error('Error fetching scheduled visits:', error);
            }
        };
    
        fetchScheduledVisits();
    }, [authenticatedUser.token]);

      console.log(availableVisits)

    return (<div>
        <div className="account-details">
            <h2 className="text-center display-6 lead welcome">Hello {user.fullName}!</h2>
            <ul className="actions navbar-m">
                <li className="nav-item"><Link to='/scheduled-appointments' className='nav-link'>Appointments</Link></li>
                <li className="nav-item"><Link to='/patient-appointment-history'  className='nav-link'>History</Link></li>
                <li className="nav-item"><Link to='/patient-to-review' className='nav-link'>Review</Link></li>
                <li className="nav-item"><Link to='/ask-a-question' className='nav-link'>Questions</Link></li>
                <li className="nav-item"><Link to='/visit-recommendations' className='nav-link'>Illnesses</Link></li>
            </ul>
            <div className="content">
                <hr></hr>
                <h4 className="incoming-visits">Your incoming visits: </h4>
                <ul className="visits">
                {availableVisits.length !== 0 ? (availableVisits.map((visit) => (
                  <li>
                  <div className="card"> 
                    <h5>{visit.doctorFirstName + " " + visit.doctorLastName}</h5>
                    <hr></hr>
                    <h6>{visit.specialization}</h6>
                    <div className="card-body">
                      <p><strong>Email:</strong> {visit.doctorEmail}</p>
                      <p><strong>Date:</strong> {visit.date && new Date(visit.date).toLocaleString()}</p>
                      <p><strong>Duration:</strong> {visit.duration} minutes</p>
                    </div>
                  </div>
                  </li>
                ))) : (
                  <div className="schedule">
                    <p>No incoming visits.</p> 
                    <Link to='/schedule-visi'>Schedule one here</Link>
                  </div>
                )}
                </ul>
            </div>
        </div>
    </div>);
}

export default Account;