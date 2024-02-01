import { useEffect, useState } from "react";
import { useAuth } from "./AuthContext"

function PatientAppointmentHistory() {
    const {user: authenticatedUser} = useAuth();
    const [visits, setVisits] = useState([]);

    useEffect(() => {
        const fetchHistory = async () => {
            try {
                const response = await fetch('/api/patient/visit-history', {
                    method: "GET",
                    headers: {"Authorization": "Bearer " + authenticatedUser.token}
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const data = await response.json();
                setVisits(data);

            } catch(error) {
                console.log("Error " + error);
            }
        }

        fetchHistory();

        console.log()

    }, [authenticatedUser.token]);

    return (
        <div className="patient-visit-history">
            { visits.length !== 0 ?
            (<div>
                <h3>You've trusted us schedulling <strong>{visits.length}</strong> visits</h3>
                <hr></hr>
                <ul>
                    {visits.map((visit) => (
                        <li key={visit.id}>
                            <div className="card">
                                <h3>{visit.doctor}</h3>
                                <hr></hr>
                                <h6>{visit.specialization}</h6>
                                <div className="card-body">
                                    <p><strong>Date: </strong>{visit.date && new Date(visit.date).toLocaleString()}</p>
                                </div>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>) :
            <div>
                <p>You haven't had any completed visits yet</p>
            </div>
            }
        </div>
    )
}

export default PatientAppointmentHistory;