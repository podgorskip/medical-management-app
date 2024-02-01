import React, { useState, useEffect } from 'react';
import { useAuth } from './AuthContext';

function Specializations() {
    const [specializations, setSpecializations] = useState([]);
    const [specializationChosen, setSpecializationChosen] = useState("");
    const [specialists, setSpecialists] = useState([]);
    const [specialistData, setSpecialistData] = useState({});

    const {user: authenticatedUser} = useAuth();

    useEffect(() => {
        fetch('api/auth/specializations', {method: "GET"})
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => setSpecializations(data))
        .catch(error => {
            console.error('Error:', error);
        });
    }, []);

    const handleSpecializationChosen = (specialization) => {
        setSpecializationChosen(specialization);
        fetch(`/api/auth/doctors-by-specialization?specialization=${specialization.id}`, {method: "GET"})
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);

      const validSpecialists = data.filter(specialist => specialist.id !== undefined);
      setSpecialists(validSpecialists);

      const fetchSpecialistDataPromises = validSpecialists.map(specialist => fetchSpecialistData(specialist));

      Promise.all(fetchSpecialistDataPromises)
        .then(() => {
          console.log("All specialist data fetched successfully");
        })
        .catch(error => {
          console.error('Error fetching specialist data:', error);
        });
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }

    const fetchSpecialistData = (specialist) => {
        return fetch(`/api/auth/available-visits?specialist=${specialist.id}`, {
            method: "GET",
        }).then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        }).then(availableVisitData => {
            const specialistData = {specialist, availableVisitData}
            console.log(specialistData)
            setSpecialistData(previousData =>  ({ ...previousData, [specialist.id]: specialistData }));
        })
    }

    const handleVisitRequest = (visit) => {

        const scheduleVisit = async () => {
            const response = await fetch(`/api/patient/schedule-visit?id=${visit.id}`, {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                throw new Error("Failed to schedule the visit");
            }
        };

        if (authenticatedUser.token.length > 0) {
            scheduleVisit();
        } else {
            alert("You need to sign in first to make an appointment");
        }
    }

    return (
        <div className="specializations">
            <h2 className='lead display-6 my-5'>Choose a specialization to check available doctors</h2>
            <div>
                <ul className='spec'>
                    {specializations.map((spec) => (
                        <li key={spec.id} onClick={() => handleSpecializationChosen(spec)}>{spec.name}</li>
                    ))}
                </ul>
                <hr></hr>

                {specializationChosen && specialists.length != 0 && (
                    <div>
                        <ul className='doctors'>
                            {specialists.map((specialist) => (
                                <li key={specialist.id}>
                                    <div className='card'>
                                        <h5>{specialist.firstName + " " + specialist.lastName}</h5>
                                        <hr></hr>
                                        <div className='card-body'>
                                            <p>{specialist.specialization}</p>
                                            <p>{specialist.email}</p>
                                            <hr></hr>
                                            <div>
                                                {specialistData[specialist.id]?.availableVisitData.length !== 0 ? (
                                                    <div>
                                                        <p className='text-center my-3 lead'>Schedule from available visits:</p>
                                                            <ul className='visits'>
                                                                {specialistData[specialist.id]?.availableVisitData.map((availableVisit) => (
                                                                    <li key={availableVisit.id}>
                                                                        <div className='card card-m'>
                                                                            <p><strong>Date: </strong> {availableVisit.date && new Date(availableVisit.date).toLocaleString()}</p>
                                                                            <p><strong>Duration: </strong> {availableVisit.duration} minutes</p>
                                                                            <button className='btn btn-primary' onClick={() => handleVisitRequest(availableVisit)}>Make an appointment</button>
                                                                        </div>
                                                                    </li>
                                                                ))}
                                                            </ul>
                                                </div>) : (
                                                    <div>No available visits</div>
                                                )}
                                            </div>
                                        </div>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}     
            </div>

        </div>      
    )
}

export default Specializations;