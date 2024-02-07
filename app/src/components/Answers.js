import { useEffect, useState } from 'react';
import { useAuth } from './AuthContext'; 

function Answers() {
    const { user: authenticatedUser } = useAuth();
    const [questions, setQuestions] = useState([]);
    const [selectedQuestion, setSelectedQuestion] = useState(null);
    const [answer, setAnswer] = useState("");

    useEffect(() => {
        const fetchQuestions = async () => {
            const response = await fetch('/api/doctor/questions', {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token
                }
            });

            if (!response.ok) {
                throw new Error("Failed to fetch questions");
            }

            const data = await response.json();
            setQuestions(data);
        };

        fetchQuestions();
    }, [authenticatedUser.token]);

    const handleQuestionSelected = (question) => {
        setSelectedQuestion(question);
    }

    const handleAnswerChange = (e) => {
        setAnswer(e.target.value);
    }

    const handleSubmitButton = (e) => {
        e.preventDefault();


        const body = {
            answer: answer
        };

        const answerQuestion = async () => {
            const response = await fetch(`/api/doctor/answer-question?id=${selectedQuestion.id}`, {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + authenticatedUser.token,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                throw new Error("Failed to answer the question");
            }

            setQuestions(prevQuestions => prevQuestions.filter(question => question.id !== selectedQuestion.id));
        };

        answerQuestion();
    }

    return (
        <div className='answers'>
            <h3>Unanswered Patient Inquiries</h3>
            <hr></hr>
            {questions.length > 0 ? (
                <>
                <ul>
                    {questions.map((question, index) => (
                        <li key={index}>
                            <div className='card'>
                                <h4>{question.patient}</h4>
                                <hr></hr>
                                <div className='card-body'>
                                    <div>
                                        <p>{question.question}</p>
                                        <p className='date'>{question.date && new Date(question.date).toLocaleDateString()}</p>
                                        <button className='btn btn-primary' onClick={() => handleQuestionSelected(question)}>Answer</button>
                                    </div>
                                    {selectedQuestion && selectedQuestion.id === question.id && (
                                        <div>
                                            <textarea value={answer} onChange={handleAnswerChange} placeholder="Type your answer here..."></textarea>
                                            <button className='btn btn-primary' onClick={handleSubmitButton} disabled={answer.length === 0}>Submit</button>
                                        </div>
                                    )}
                                </div>
                            </div>
                        </li>
                    ))}
                </ul>
                </>
            ) : (
                <div>
                    <p>No questions waiting to be answered</p>
                </div>

            )}


        </div>
    );
}

export default Answers;