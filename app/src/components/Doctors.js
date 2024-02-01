import { useState, useEffect } from 'react'
import star from "../images/star.png"

function Doctors() {
    const [specialists, setSpecialists] = useState([]);
    const [reviews, setReviews] = useState({})

    useEffect(() => {
        const fetchReviews = async (doctor) => {
            try {
                const response = await fetch(`/api/auth/reviews?id=${doctor.id}`, {
                    method: "GET"
                });
    
                if (!response.ok) {
                    throw new Error("Failed to fetch reviews");
                }
    
                const reviewData = await response.json();
                setReviews(previousData => ({ ...previousData, [doctor.id]: { doctor, reviews: reviewData } }));
            } catch (error) {
                console.error("Error fetching reviews:", error);
            }
        };
    
        const fetchData = async () => {
            try {
                const response = await fetch('/api/auth/doctors', {
                    method: "GET"
                });

                if (!response.ok) {
                    throw new Error("Failed to fetch doctors");
                }

                const data = await response.json();
                setSpecialists(data);

                const fetchedReviews = data.map(specialist => fetchReviews(specialist));
                await Promise.all(fetchedReviews);

                console.log("All specialist data fetched successfully");
            } catch (error) {
                console.error("Error:", error);
            }
        };

        fetchData();
    }, []);
    



    return (
        <div className='doctors'>
            <h2>Our exceptional team of doctors</h2>
            <p>
                <span>Welcome</span> to our prestigious medical team, where expertise meets compassion. Our dedicated doctors are committed to providing top-notch healthcare services. 
                Here's a glimpse of our esteemed specialists and what our patients have to say about their experiences:
            </p>
                <ul>
                    {
                        specialists.map((specialist) => (
                            <li>
                                <div className='card'>
                                    <h3>{specialist.firstName + " " + specialist.lastName}</h3>
                                    <hr/>
                                    <h5>{specialist.specialization}</h5>
                                    <hr/>
                                    {reviews[specialist.id] ? (
                                        <ul className='reviews'>
                                            {
                                                reviews[specialist.id].reviews.map((review) => (
                                                    <li>
                                                        <div className='review'>
                                                            <div>
                                                                {review.comment}
                                                                <div className='stars'>
                                                                    {Array.from({length : review.rating}, (_, idx) => (
                                                                    <img src={star} width={20} height={20}/>
                                                                    ))}
                                                                </div>
                                                            </div>
                                                            <div>
                                                                <p>{review.reviewDate && new Date(review.reviewDate).toDateString()}</p>
                                                                <p>{review.patient}</p>
                                                            </div>
                                                        </div>
                                                        <hr></hr>
                                                    </li>
                                                ))
                                            }
                                        </ul>
                                       
                                    ) : (
                                        <div>dd</div>
                                    )
                                        
                                    }
                                </div>
                            </li>
                        ))
                    }
                </ul>
        </div>
    )
}

export default Doctors;