import "./Portail.scss";
import img1 from "assets/images/background.jpg"


function Portail() {
  return (
    <div className="portail-container">
      
      <h3 className="text-style-portail">Films, séries TV et bien plus en illimité.</h3>
      <p className="text-style-portail">Où que vous soyez. Annulez à tout moment.</p>
      <img src={img1} width="500"/>
      <h3> Profitez d'un service de streaming de qualité </h3>
    </div>
  );
}

export default Portail;
