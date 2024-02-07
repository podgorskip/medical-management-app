import { useState, useEffect } from 'react';
import { useAuth } from './AuthContext';

function Prescriptions() {
    const {user: authenticatedUser} = useAuth();
    const [prescriptions, setPrescriptions] = useState([]);

    useEffect(() => {
        const fetchPrescriptions = async () => {
            const response = await fetch('/api/patient/my-prescriptions', {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                console.log("No prescriptions");
                return;
            }

            const data = await response.json();
            setPrescriptions(data);
        };

        fetchPrescriptions();

    }, [authenticatedUser.token]);

    return (
        <div className='prescriptions'>
            <h3>My prescriptions</h3>
            <hr></hr>
            {prescriptions.length > 0 ? (
                <div>
                    <ul>
                        {prescriptions.map((prescription) => (
                            <li key={prescription.code}>
                                <div className='card'>
                                    <h4>Prescription {prescription.code}</h4>
                                    <hr/>
                                    <div>
                                        <p><strong>Medications:</strong></p>
                                        <ul>
                                            {prescription.medications.map((med) => (
                                                <li>{med}</li>
                                            ))}
                                        </ul>
                                        <p><strong>Description: </strong>{prescription.description}</p>
                                        <p><strong>Expiration: </strong> {prescription.expirationDate && new Date(prescription.expirationDate).toLocaleString()}</p>
                                    </div>
                                    <div>
                                        <img src={`data:image/png;base64, ${prescription.barcode}`} width={100}/>
                                        <p>{prescription.doctor}</p>
                                        <p>{prescription.issueDate && new Date(prescription.issueDate).toLocaleString()}</p>
                                    </div>
                                   
                                </div>
                            </li>
                        ))}
                    </ul>
                </div>
            ) : (
                <div>
                    <p>You haven't got any prescriptions yet.</p>
                </div>
            )}

        </div>
    );
}

export default Prescriptions;