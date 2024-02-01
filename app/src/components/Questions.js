import { useState, useEffect } from 'react'
import { useAuth } from './AuthContext'

function Questions() {
    const {user : authenticatedUser} = useAuth();
    const [specializations, setSpecializations] = useState([]);
    const [specializationChosen, setSpecializationChosen] = useState({});
    const [questionContent, setQuestionContent] = useState('');

    useEffect(() => {
        const fetchSpecializations = async () => {
            const response = await fetch('/api/auth/specializations', {
                method: "GET"
            });

            if (!response.ok) {
                throw new Error("Failed to fetch specializations");
            }

            const data = await response.json();
            setSpecializations(data);
        };

        fetchSpecializations();

    }, [])

    const handleSpecializationChosen = (specialization) => {
        setSpecializationChosen(specialization);
    }

    const handleQuestionContentSet = (e) => {
        setQuestionContent(e.target.value);
    }

    const handleSubmit = () => {
        console.log('clicked')
    }



    return (
        <div className='questions-panel'>
            <h5>Our team of specialists is here to assist you by offering valuable insights and addressing your inquiries with expertise and compassion. </h5>
            <h6>Feel free to ask any questions you may have, and we'll provide professional advice tailored to your needs.</h6>
            <hr/>
            {specializations.length !== 0 ? (
                <div>
                    <ul className='specializations'>
                        {specializations.map((specialization) => (
                            <li key={specialization.id} onClick={() => handleSpecializationChosen(specialization)}>{specialization.name}</li>
                        ))}
                     </ul>
                     <hr/>
                </div>
            ) : (
                <p>No available specializations</p>
            )}
            {specializationChosen.id !== null && (
                <div className='ask-question'>
                    <h6>{specializationChosen.name}</h6>
                    <input type='text' onChange={handleQuestionContentSet} placeholder='Ask a question...'></input>
                    <button className='btn btn-primary' onClick={handleSubmit} disabled={!specializationChosen.id || questionContent.length === 0}>Submit</button>
                </div>
            )}
            
        </div>
    )
}

export default Questions;