function SummaryCard({ title, value, children }) {
  return (
    <div className="card h-100 shadow-sm">
      <div className="card-body">
        <h6 className="card-subtitle mb-3 text-body-secondary">{title}</h6>

        {children ? children : <h3 className="card-title mb-0">{value}</h3>}
      </div>
    </div>
  );
}

export default SummaryCard;
