import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Pie } from "react-chartjs-2";

ChartJS.register(ArcElement, Tooltip, Legend);

const palette = [
  "#2EC4B6",
  "#FF9F68",
  "#FFD166",
  "#5B8DEF",
  "#B39DDB",
  "#EF6F91",
  "#7BC8A4",
  "#F4A261",
  "#6C9A8B",
  "#A8DADC",
];

function IncomeExpensePieChart({ data = [] }) {
  const chartData = {
    labels: data.map((item) => item.categoryName),
    datasets: [
      {
        data: data.map((item) => item.amount),
        backgroundColor: data.map((_, index) => palette[index % palette.length]),
        borderColor: "#FFFFFF",
        borderWidth: 3,
        hoverOffset: 8,
      },
    ],
  };

  return (
    <div className="chart-container">
      <Pie
        data={chartData}
        options={{
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: "bottom",
              labels: { usePointStyle: true, padding: 18 },
            },
          },
        }}
      />
    </div>
  );
}

export default IncomeExpensePieChart;
