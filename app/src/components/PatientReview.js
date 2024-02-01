import { useEffect, useState } from "react"
import { useAuth } from "./AuthContext"
import { useNavigate } from "react-router-dom"

function PatientReview() {
    const {user : authenticatedUser} = useAuth();
    const [toReview, setToReview] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchToReview = async () => {
            try {
                const response = await fetch("/api/patient/to-review", {
                    method: "GET",
                    headers: {"Authorization": "Bearer " + authenticatedUser.token}
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const data = await response.json();
                setToReview(data);

            } catch (error) {
                console.log("Error " + error);
            }
        }

        fetchToReview();
        console.log(toReview)

    }, [authenticatedUser.token]);

    const handleReviewChosen = (review) => {
        navigate(`/write-review/${review.id}`);
    }


    return (
        <div className="to-review">
            <h3>Your opinion matters to us</h3>
            <h6>Share your thoughts on your recent visits</h6>
            <hr></hr>
            {toReview.length !== 0 ? (
                <ul>
                    {toReview.map((review) => (
                        <li key={review.id}>
                            <div className="card">
                                <div>
                                    <div>
                                        <h3>{review.doctor}</h3>
                                        <hr/>
                                    </div>
                                    <div>
                                        <p><strong>Date: </strong>{review.date && new Date(review.date).toLocaleString()}</p>
                                        <p><strong>Specialization: </strong>{review.specialization}</p>
                                    </div>

                                </div>
                                <button onClick={() => handleReviewChosen(review)} className="btn btn-primary">Review</button>
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <div></div>
            )

            }
        </div>
    )
}

export default PatientReview;