import { useState, useEffect } from 'react';
import { useAuth } from "./AuthContext"; 
import { Link } from "react-router-dom";

function IncomingAppointments() {
    const [availableVisits, setAvailableVisits] = useState([]);
    const { user: authenticatedUser } = useAuth();

    useEffect(() => {
        const fetchScheduledVisits = async () => {
            try {
                const response = await fetch('/api/patient/scheduled-visits', {
                    method: "GET",
                    headers: {"Authorization": "Bearer " + authenticatedUser.token}
                });
    
                if (!response.ok) {
                  console.log("No available visits");
                  return;
                }
    
                const data = await response.json();
                setAvailableVisits(data);
            } catch (error) {
                console.error('Error fetching scheduled visits:', error);
            }
        };
    
        fetchScheduledVisits();
    }, [authenticatedUser.token]);

    return (
        <div className="x">
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
    )
}

export default IncomingAppointments;