import { Link, useNavigate } from "react-router-dom";

function Locked() {
  return (
    <div>
      Vous n'avez pas le droit d'accès.
      <br />
      <br />
      <Link to="/">Retour à l'accueil</Link>
    </div>
  );
}

export default Locked;
