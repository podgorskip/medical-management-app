import {useEffect, useState} from "react";
import { Link } from "react-router-dom";
import { useAuth } from "./AuthContext";

function Appointments() {
    const {user: authenticatedUSer} = useAuth();
    const [appointments, setAppointments] = useState([]);

    useEffect(() => {
           const fetchAppointments = async () => {
            try {
               const response = await fetch('/api/patient/scheduled-visits', {
                   method: "GET",
                   headers: {"Authorization": "Bearer " + authenticatedUSer.token}
               });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const data = await response.json();
                setAppointments(data);
            } catch (error) {
                console.log("Error " + error);
            }
        }

       fetchAppointments();

    }, [authenticatedUSer.token])

    return (
        <div className="appointments">
            <h3>Your appointments</h3>
            <hr></hr>
            <div>
                {appointments.length !== 0 ? (
                    <ul>
                        {appointments.map((appointment) => (
                            <li key={appointment.id}>
                                <div className="card">
                                    <h5>{appointment.doctorFirstName + " " + appointment.doctorLastName}</h5>
                                    <hr></hr>
                                    <div className="card-body">
                                        <h6>{appointment.specialization}</h6>
                                        <hr></hr>
                                        <p><strong>Date: </strong>{appointment.date && new Date(appointment.date).toLocaleString()}</p>
                                        <p><strong>Duration: </strong>{appointment.duration} minutes</p>
                                    </div>
                                </div>
                            </li>
                        ))}
                    </ul>
                ) : (
                    <h5>No visits</h5>
                )}
            </div>

        </div>
    )

}

export default Appointments;