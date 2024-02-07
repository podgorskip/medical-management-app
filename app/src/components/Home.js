import logo from '../images/logo.png'

function Home() {
    return(
        <div className="home-page">
            <img src={logo} width={550} height={200} alt="logo"/>
            <div className="header">
            
                <h2>Welcome to WellnessHub!</h2>
                <div>
                    <h4>Your trusted partner in healthcare.</h4>
                    <h6>Discover, schedule, and manage your appointments with ease.</h6>
                </div>
            </div>
            <div>
                <p>
                    <span>WellnessHub, your all-in-one platform designed to empower your journey towards optimal health and well-being. </span>
                    WellnessHub offers a suite of features that cater to your healthcare needs, ensuring a seamless and personalized experience.
                </p>
                <ul className='functions'>
                    <li>
                        <div className='card'>
                            <h5>Schedule visits to specialists</h5>
                            <hr/>
                            <p>
                                Easily book appointments with a diverse range of healthcare specialists. 
                                Our intuitive scheduling system allows you to choose the date and time that suits your convenience.
                            </p>
                        </div>
                    </li>
                    <li>
                        <div className='card'>
                            <h5>Visit history</h5>
                            <hr/>
                            <p>
                                Keep track of your medical journey with a detailed visit history. 
                                Access past appointments, diagnoses, and treatments all in one place for a comprehensive overview of your health.
                            </p>
                        </div>
                    </li>
                    <li>
                        <div className='card'>
                            <h5>Ask questions to specialists</h5>
                            <hr/>
                            <p>
                                Connect with healthcare experts directly through our platform. 
                                Ask questions, seek advice, and gain insights from specialists to make informed decisions about your health.
                            </p>
                        </div>
                    </li>
                    <li>
                        <div className='card'>
                            <h5>Write reviews after appointments</h5>
                            <hr/>
                            <p>
                                Share your experiences by leaving reviews after each appointment. 
                                Your feedback contributes to a community-driven approach, helping others make informed choices when selecting healthcare providers.
                            </p>
                        </div>
                    </li>
                    <li>
                        <div className='card'>
                            <h5>Get email notifications</h5>
                            <hr/>
                            <p>
                                Stay informed about upcoming appointments, prescription renewals, and important updates with our email notification system. 
                                Never miss a crucial healthcare event again.
                            </p>
                        </div>
                    </li>
                    <li>
                        <div className='card'>
                            <h5>Prescription management</h5>
                            <hr/>
                            <p>
                                Centralize all your prescriptions in one secure location. 
                                Access and manage your medication information, ensuring a comprehensive overview of your treatment plans.
                            </p>
                        </div>
                    </li>
                </ul>
                <p>
                    WellnessHub is committed to providing a holistic and user-friendly healthcare experience. 
                    Whether you're proactively managing your well-being or addressing specific health concerns, 
                    our platform is your dedicated partner on the path to a healthier life.
                </p>
            </div>
        </div>
    )
}

export default Home;