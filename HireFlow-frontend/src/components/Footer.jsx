function Footer() {
  return (
    <footer className="bg-gray-900 text-gray-300 mt-16">
      <div className="max-w-6xl mx-auto px-6 py-8 grid md:grid-cols-3 gap-8">

        <div>
          <h3 className="text-white text-xl font-semibold mb-3">HireFlow</h3>
          <p className="text-sm">
            Connecting talent with opportunity.  
            A seamless recruitment experience for employers and professionals.
          </p>
        </div>

        <div>
          <h4 className="text-white font-semibold mb-3">Contact</h4>
          <p className="text-sm text-gray-400 mt-2">
            support@hireflow.com
          </p>
        </div>

      </div>

      <div className="text-center py-4 border-t border-gray-700 text-sm">
        © {new Date().getFullYear()} HireFlow. All rights reserved.
      </div>
    </footer>
  );
}

export default Footer;