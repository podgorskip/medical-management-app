import { useState, useEffect } from 'react';
import { useAuth } from './AuthContext';

function AddVisits() {
    const { user: authenticatedUser} = useAuth();
    const [slots, setSlots] = useState([]);
    const [date, setDate] =  useState(new Date().toISOString().slice(0,16));
    const [duration, setDuration] = useState("");
    const [isAdded, setIsAdded] = useState(false);

    const handleDateChange = (e) => {
        setDate(e.target.value);
    }

    const handleDurationChange = (e) => {
        setDuration(e.target.value);
    }

    const handleDateSubmit = (e) => {
        e.preventDefault();

        const visit = {
            date: date,
            duration: duration
        };

        setSlots((prevSlots) => [...prevSlots, visit]);
    };

    const handleDateRemoval = (idx) => {
        setSlots(prevSlots => {
            return prevSlots.filter((_, index) => index !== idx);
        });
    };

    const handleAddVisitSubmit = (e) => {
        e.preventDefault();

        const body = {
            visits: slots
        };

        const addVisits = async () => {
            const response = await fetch('/api/doctor/add-visits', {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token,
                    "Content-Type": "application/json"
                }, 
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                throw new Error("Failed to add visits");
            }

            setIsAdded(true);
            setSlots([]);
        };

        addVisits();
    }

    return (
        <div className='add-visits'>
            <h3>Introduce available visit slots</h3>
            <hr></hr>
            <div>
                <h5>Select a date and time</h5>
                <input type='datetime-local' onChange={handleDateChange} value={date} min={new Date().toISOString().slice(0,16)}></input>
                <input type='number' min="20" onChange={handleDurationChange} value={duration} max="90" step="5"></input>
                <button className='btn btn-primary' onClick={handleDateSubmit}>Add</button>
            </div>
            <hr></hr>
            {slots.length > 0 && (
                <>
                    <ul>
                    { slots.map((slot, index) => (
                        <li key={index}>
                            <label htmlFor={index}>{new Date(slot.date).toLocaleString().slice(0, 17) + " - " + slot.duration + " minutes"}</label>
                            <button onClick={() => handleDateRemoval(index)} id={index} className='btn btn-primary'>X</button>
                        </li>
                    ))
                    }
                    <hr></hr>
                    </ul>
                    <button className='btn btn-primary' onClick={handleAddVisitSubmit}>Add visits</button>
                </>
            )}
            {isAdded && (
                <div className='alert alert-success'>
                    Successfully added available slots
                </div>
            )}
        </div>
    )
}

export default AddVisits;