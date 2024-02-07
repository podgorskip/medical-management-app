import { useParams } from "react-router-dom";
import { useAuth } from './AuthContext';
import { useEffect, useState } from 'react';

function PatientPanel() {
    const { patient } = useParams();
    const { user : authenticatedUser } = useAuth();
    const [medications, setMedications] = useState([]);
    const [selectedMedications, setSelectedMedications] = useState([]);
    const [recommendations, setRecommendations] = useState("");
    const [isPrescribed, setIsPrescribed] = useState(false);

    useEffect(() => {
        const fetchMedications = async () => {
            const reponse = await fetch('/api/doctor/medications', {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!reponse.ok) {
                throw new Error("Failed to fetch medications");
            }

            const data = await reponse.json();
            setMedications(data);
        }

        fetchMedications();

    }, [authenticatedUser.token]);

    const handleMedicationChosen = (medication) => {
        const isSelected = selectedMedications.includes(medication.id);

        if (!isSelected) {
            setSelectedMedications(prevMedications => [...prevMedications, medication.id]);
        } else {
            setSelectedMedications(prevMedications => prevMedications.filter(id => id !== medication.id));
        }
    }

    const handleRecommnedationsInputChange = (e) => {
        setRecommendations(e.target.value);
    }

    const clearObject = (e) => {
        e.preventDefault();

        setSelectedMedications([]);

        const input = document.querySelector("input[type='text']");
        input.value = "";

        const checkboxes = document.querySelectorAll("input[type='checkbox']")
        checkboxes.forEach((checkbox) => {
            checkbox.checked = false;
        })
    }

    const handleSubmit = (e) => {
        e.preventDefault();

        const body = {
            patientID: patient,
            medicationsID: selectedMedications,
            description: recommendations
        };

        const prescribeMedications = async () => {
            const response = await fetch('/api/doctor/prescribe-medications', {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                throw new Error("Failed to prescribe medications");
            }

            setIsPrescribed(true);
            clearObject(e);
        }

        prescribeMedications();
    }

    return (
        <div className="patient-panel">
            <h3>Patient panel</h3>
            <hr></hr>
            <h5>Available medications to prescribe</h5>
            <ul className="medications">
                {medications.length > 0 && (
                    medications.map((medication) => (
                        <li className="hover-container">
                            <input type="checkbox" id={"med-" + medication.id} onChange={() => handleMedicationChosen(medication)}></input>
                            <label htmlFor={"med-" + medication.id} >{medication.name + " [" + medication.dose + " mg]"}</label>
                        </li>
                    ))
                )}
            </ul>
            <h5>Usage recommendations</h5>
            <input type='text' onChange={handleRecommnedationsInputChange} placeholder="Type here usage recommendations..."></input>
            <button className="btn btn-primary" onClick={handleSubmit} disabled={recommendations.length <= 0}>Prescribe</button>
            {isPrescribed && (
                <div class="alert alert-success" role="alert">
                    Successfully issued the prescription
                </div>
            )
            }
        </div>
    );
}

export default PatientPanel;