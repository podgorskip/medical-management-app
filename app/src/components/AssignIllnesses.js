import { useState, useEffect } from 'react';
import { useAuth } from './AuthContext'

function AddIllnessesComponent() {
  const [illnesses, setIllnesses] = useState([]);
  const [selectedIllness, setSelectedIllness] = useState([]);
  const [submitted, setSubmitted] = useState(false);

  const {user: authenticatedUser} = useAuth();

  useEffect(() => {
    fetch('/api/auth/illnesses') 
      .then((response) => response.json())
      .then((data) => setIllnesses(data))
      .catch((error) => console.error('Error fetching illnesses:', error));
  }, []);

  const handleCheckboxChange = (illness) => {
    setSelectedIllness((prevSelected) => 
    prevSelected.includes(illness.id) ? prevSelected.filter((id) => id !== illness.id) : [...prevSelected, illness.id])
  }

  const handleSubmit = () => {

    const requestData = {
      illnesses: selectedIllness
    };

    const addIllnessesToAccount = async () => {
      const response = await fetch(`/api/patient/add-illnesses`, {
        method: "POST",
        headers: {"Authorization": "Bearer " + authenticatedUser.token, 
                  "Content-Type": "application/json"},
        body: JSON.stringify(requestData)
      });

      if (!response.ok) {
        throw new Error("Failed to assign illnesses");
      }
    }

  
    console.log(selectedIllness)

    addIllnessesToAccount();
    setSubmitted(true);
  }

  return (
    <div className="illness-page">
      <h1 className='lead display-6 my-5'>Our specialists can help you with many illnesses</h1>
      <hr/>
      <h5>Please select from the list of illnesses below to receive personalized visit recommendations:</h5>
      <ul className='illnesses'>
        {illnesses.map((illness) => (
          <li key={illness.id}>
            <input type='checkbox' onChange={() => handleCheckboxChange(illness)} name='illnesses' id={illness.id}></input>
            <label htmlFor={illness.id}>{illness.name}</label>
          </li>
        ))}
      </ul>
      <hr></hr>

      {selectedIllness.length > 0 && (
        <div>
          <button onClick={handleSubmit} className='btn btn-primary'>Submit</button>
          {
            submitted && (
              <h6>Successfully assigned illnesses to your account</h6>
            )
          }
        </div>
      )}
    </div>
  );
}

export default AddIllnessesComponent;