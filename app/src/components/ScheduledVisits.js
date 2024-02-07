import { useState, useEffect } from 'react';
import { useAuth } from "./AuthContext";

function ScheduledVisits() {
    const { user: authenticatedUser } = useAuth();
    const [visits, setVisits] = useState([]);

    useEffect(() => {
        const fetchVisits = async () => {
            const response = await fetch('/api/doctor/scheduled-visits', {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("No available visits");
                return;
            }

            const data = await response.json();
            setVisits(data);
        };

        fetchVisits();

        console.log(visits)
    }, [authenticatedUser.token]);

    return (
        <div className='doctors-scheduled-visits'>
            <h3>Your incoming visits</h3>
            <hr></hr>
            {visits.length > 0 ? (
                <ul>
                    {visits.map((visit, index) => (
                        <li key={index}>
                            <div className='card'>
                                <h5>{visit.patientFirstName + " " + visit.patientLastName}</h5>
                                <hr></hr>
                                <p><strong>Email: </strong>{visit.patientEmail}</p>
                                <p><strong>Date: </strong>{visit.date}</p>
                                <p><strong>Duration: </strong>{visit.duration}</p>
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No incoming visits</p>
            )}

        </div>
    )
}

export default ScheduledVisits;