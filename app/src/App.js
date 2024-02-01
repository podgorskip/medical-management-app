import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './Home';
import SignIn from './components/SignIn';
import SignUp from './components/SignUp';
import AddIllnessesComponent from './components/AddIllnessesComponent';
import Specializations from './components/Specializations';
import { AuthProvider } from './components/AuthContext';
import Account from './components/Account';
import Appointments from './components/Appointments';
import PatientAppointmentHistory from "./components/PatientAppointmentHistory"
import Footer from './components/Footer';
import PatientReview from './components/PatientReview';
import ReviewPanel from './components/ReviewPanel'
import Doctors from './components/Doctors'
import Questions from './components/Questions';
import VisitsReccomendationsComponent from './components/VisitsReccomendationsComponent';

function App() {
  return (
    <div className='container'>
      <Router>
        <Navbar />
        <AuthProvider>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/signin" element={<SignIn />} />
              <Route path="/signup" element={<SignUp />} />
              <Route path="/specializations" element={<Specializations />} />
              <Route path='/my-account' element={<Account />} />
              <Route path='/scheduled-appointments' element={<Appointments />}/>
              <Route path='/patient-appointment-history' element={<PatientAppointmentHistory />}/>
              <Route path='/patient-to-review' element={<PatientReview />} />
              <Route path="/write-review/:id" element={<ReviewPanel />} />
              <Route path='/doctors' element={<Doctors />} />
              <Route path='/ask-a-question' element={<Questions />} />
              <Route path='/select-illnesses' element={<AddIllnessesComponent />} />
              <Route path='/visit-recommendations' element={<VisitsReccomendationsComponent/>} />
            </Routes>
        </AuthProvider>
        <Footer />
      </Router>
    </div>
  );
}

export default App;

