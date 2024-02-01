import { useState, useEffect } from 'react'
import { useAuth } from './AuthContext'

function VisitsReccomendationsComponent() {
    const [visitRecommendations, setVisitrecommendations] = useState({});
    const [visitSelected, setVisitSelected] = useState("");
    const {user: authenticatedUser } = useAuth();

    useEffect(() => {
        const fetchMyIllnesses = async () => {
            const response = await fetch('/api/patient/visit-recommendations', {
                method: "GET",
                headers: {"Authorization": "Bearer " + authenticatedUser.token}
            });

            if (!response.ok) {
                throw new Error("Failed to fetch illnesses");
            }

            const data = await response.json();
            setVisitrecommendations(data);
        }
        
        fetchMyIllnesses();
        console.log(visitRecommendations);

    }, [authenticatedUser.token, visitSelected]);

    const handleVisitSelected = (visit) => {
        const scheduleVisit = async () => {
            const response = await fetch(`/api/patient/schedule-visit?id=${visit.id}`, {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token,
                }
            });

            if (!response.ok) {
                throw new Error("Failed to schedule the visit");
            }
        }

        scheduleVisit();
        setVisitSelected(visit.id);
    }

    return (
        <div className='visit-recommendations'>
            {Object.keys(visitRecommendations).length > 0 ? (
        <div>
          <h3>Based on your chosen illnesses</h3>
          <h6>
            We've tailored our recommendations to align with your selected illnesses.
            Here's what we found for you
          </h6>
          <hr />
          <ul>
          {Object.entries(visitRecommendations).map(([illness, visits]) => (
        <div key={illness}>
            <h5>{illness}</h5>
            <ul className='recommendation'>
                {visits.map((visit) => (
                    <li key={visit.id}>
                        <div className='card'>
                            <h4>{visit.doctorFirstName + " " + visit.doctorLastName}</h4>
                            <hr/>
                            <p><strong>Specialization: </strong>{visit.specialization}</p>
                            <p><strong>Date: </strong>{visit.date && new Date(visit.date).toLocaleString()}</p>
                            <p><strong>Duration: </strong>{visit.duration} minutes</p>
                            <button className='btn btn-primary' onClick={() => handleVisitSelected(visit)}>Make an appointment</button>
                        </div>
                    </li>
                ))}
            </ul>
            <hr></hr>
        </div>
    ))}
          </ul>
        </div>
      ) : (
        <div>No visit recommendations available.</div>
      )}
           
        </div>
    )
}

export default VisitsReccomendationsComponent;
