import { useEffect, useState } from "react"
import { useParams } from 'react-router-dom'
import star from '../images/star.png'
import { useAuth } from './AuthContext'

function ReviewPanel() {
    const { id } = useParams();
    const [rating, setRating] = useState("");
    const [comment, setComment] = useState("");
    const [isSubmitted, setIsSubmitted] = useState(false);
    const { user : authenticatedUser } = useAuth();

    console.log(id);

    const handleRatingChange = (e) => {
        setRating(e.target.value);
    }

    const handleCommentChange = (e) => {
        setComment(e.target.value);
    }

    const handleSubmit = async(e) => {
        e.preventDefault();

    const formData = new FormData();

    try {
        const request = {
            rating: rating,
            comment: comment
        };

        const response = await fetch(`/api/patient/review?id=${id}`, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + authenticatedUser.token,
                "Content-Type": "application/json" 
            },
            body: JSON.stringify(request),
        });

        if (!response.ok) {
            throw new Error("Failed to send the request");
        }

        setIsSubmitted(true);
    } catch (error) {
        console.error("Error sending the request:", error);
    }

        setIsSubmitted(true);
    }

    return (
        <div className="review-panel">
            <div>
                <h4>Fill in the form and help us gather valuable information. </h4>
                <h5> Your input is essential in improving our services. </h5>
            </div>
            <form>
                {rating && (
                    <div>
                        {Array.from({length : rating}, (_, idx) => (
                            <img src={star} width={20} height={20}/>))
                        }
                    </div>
                )}
                <label>Rating </label>
                <input value={rating} onChange={handleRatingChange}  type="range" min="1" max="10"/>
                <label>Comment </label>
                <input value={comment} onChange={handleCommentChange} type="text" placeholder="Share your thoughts..."/>
                <button onClick={handleSubmit} className="btn btn-primary">Submit</button>
            </form>
            {isSubmitted && (
                <h6>Thank you for taking the time to share your thoughts!</h6>
            )}
        </div>
    )

}

export default ReviewPanel;