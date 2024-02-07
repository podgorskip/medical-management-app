import { useEffect, useState } from 'react';
import { useAuth } from "./AuthContext";
import { useNavigate } from 'react-router-dom';

function TodayVisits() {
    const [todayVisits, setTodayVisits] = useState([]);
    const navigate = useNavigate();
    const { user: authenticatedUser } = useAuth();

    useEffect(() => {
        const fetchTodayVisits = async () => {
          const response = await fetch('/api/doctor/today-visits', {
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
          setTodayVisits(data);
        };
    
        fetchTodayVisits();
    }, [authenticatedUser.token]);
    
    const handleVisitChosen = (visit) => {
        navigate(`/patient-panel/${visit.patientID}`);
    }

    return (
        <div>
            <h4>Today's scheduled appointments</h4>
                <ul>
                    {todayVisits.length > 0 ? (
                        todayVisits.map((visit) => (
                        <li>
                            <div className="card"> 
                              <h5>{visit.patientFirstName + " " + visit.patientLastName}</h5>
                              <hr></hr>
                              <div className="card-body">
                                <p><strong>Email:</strong> {visit.patientEmail}</p>
                                <p><strong>Duration:</strong> {visit.duration} minutes</p>
                                <button className="btn btn-primary" onClick={() => handleVisitChosen(visit)}>Manage visit</button>
                              </div>
                            </div>
                        </li>
                    ))) : (
                        <div>
                          <p>No scheduled appointments for today</p>
                        </div>
                      )}
                </ul>
        </div>
    );
}

export default TodayVisits;